package MaxTheMeteorStrike.jdx;

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
        position = new Vector2(1024 * (float) Math.random(), 720 * (float) Math.random());
        setSpeed();
        setScale();
        rotation = (float) (Math.random() * 360);
    }

    public void update(float dt) {
        position.x -= (int) (speed + dt);
        rotation += 5;
        if (position.x < -50) {
            position.x = 1300;
            position.y = 720 * (float) Math.random();
            setSpeed();
            setScale();
        }
    }
    private void setSpeed(){
        speed = (float) (10.f + Math.random() * 15.f);
    }
    private void setScale(){
        scale = (float) (0.75f + Math.random() * 0.5f);
    }
    public void render(SpriteBatch batch) {
        batch.draw(image, position.x, position.y, 8, 8, 16, 16, scale, scale, rotation, 0, 0, 32, 32, false, false);
    }
}
