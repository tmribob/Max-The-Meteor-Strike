package MaxTheMeteorStrike.jdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class CosmicObjects {
    protected Vector2 position;
    protected byte hp;
    protected float speed;
    protected float scale;
    protected Texture image;
    protected float width;
    protected float height;
    protected boolean isActive;

    public CosmicObjects(byte hp, float speed,Texture image,float scale){
        this.hp = hp;
        this.speed = speed;
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
        position = new Vector2(Gdx.graphics.getWidth()+  width /2, Gdx.graphics.getHeight()+height);
        this.scale=scale;
        isActive=false;
    }
    public void update(float dt) {
        position.x -= (int) (speed + dt);
        if (position.x < -width /2 || hp==0) {
            setActive(false);
        }
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void setPosition() {
        position.x = MathUtils.random(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() + 200);
        position.y = MathUtils.random(height / 2, Gdx.graphics.getHeight() - height / 2);
    }

    public Vector2 getPosition() {
        return position;
    }
    public void render(SpriteBatch batch) {
        batch.draw(image, position.x - width / 2, position.y - height / 2, width / 2, height / 2,
            width, height, scale, scale, 0, 0, 0, (int) width, (int) height, false, false);
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
