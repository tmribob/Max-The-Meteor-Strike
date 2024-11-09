package MaxTheMeteorStrike.jdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Star {
    private final Vector2 position;
    private short speed;
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
        position.x -= speed * dt;
        rotation += 5;
        if (position.x < -50) {
            position.x = Gdx.graphics.getWidth();
            position.y = MathUtils.random(0,Gdx.graphics.getHeight());
            setSpeed();
            setScale();
        }
    }

    private void setSpeed() {
        speed = (short) MathUtils.random(900,1900);
    }

    private void setScale() {
        scale = (float) (0.1f + Math.random() * 0.25f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x, position.y, (float) image.getWidth() /2, (float) image.getHeight() /2,
                image.getWidth(), image.getHeight(), scale, scale, rotation, 0, 0, image.getWidth(), image.getHeight(), false, false);
    }
}
