package MaxTheMeteorStrike.jdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Star {
    private final Vector2 position;
    private float speed;
    private float rotation;
    private float scale;

    private static final Texture image = new Texture("star.png");

    public Star() {
        position = new Vector2(Gdx.graphics.getWidth() * (float) Math.random(), Gdx.graphics.getHeight() * (float) Math.random());
        setSpeed();
        setScale();
        rotation = (float) (Math.random() * 360);
    }

    public void update(float dt) {
        position.x -= (int) (speed + dt);
        rotation += 5;
        if (position.x < -50) {
            position.x = Gdx.graphics.getWidth();
            position.y = Gdx.graphics.getHeight() * (float) Math.random();
            setSpeed();
            setScale();
        }
    }

    private void setSpeed() {
        speed = (float) (15.f + Math.random() * 15.f);
    }

    private void setScale() {
        scale = (float) (0.3f + Math.random() * 0.25f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x, position.y, 8, 8, 16, 16, scale, scale, rotation, 0, 0, 32, 32, false, false);
    }
}
