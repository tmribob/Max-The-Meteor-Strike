package MaxTheMeteorStrike.jdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public abstract class CosmicObjects {
    protected Vector2 position;
    protected byte hp;
    protected short speed;
    protected float scale;
    protected Texture image;
    protected float width;
    protected float height;
    private boolean isActive;
    protected byte rotation;

    public CosmicObjects(byte hp, short speed, Texture image, float scale, byte rotation) {
        this.hp = hp;
        this.speed = speed;
        this.image = image;
        this.scale = scale;
        width = image.getWidth() * scale;
        height = image.getHeight() * scale;
        position = new Vector2(Gdx.graphics.getWidth() + width / 2, Gdx.graphics.getHeight() + height);
        isActive = false;
        this.rotation = rotation;
    }

    public void update(float dt) {
        position.x -= speed * dt;
        if (position.x < -width / 2 || hp == 0) {
            destroy();
        }
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setRandomPosition() {
        position.x = MathUtils.random(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() + 200);
        position.y = MathUtils.random(height / 2, Gdx.graphics.getHeight() - height / 2);
    }
    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x - width / 2, position.y - height / 2, width / 2, height / 2,
            width, height, scale, scale, rotation, 0, 0, (int) width, (int) height, false, false);
    }


    public boolean isActive() {
        return isActive;
    }

    public void destroy() {
        isActive = false;
        hp = 0;
        position.set(Gdx.graphics.getWidth() + width / 2, Gdx.graphics.getHeight() + height);

    }

    public void revive() {
        hp = 1;
        isActive = true;
        setRandomPosition();
    }
}
