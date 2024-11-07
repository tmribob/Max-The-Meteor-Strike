package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private final Vector2 position;
    private float speed;
    private float rotation;
    private float scale;
    private int hp;
    private Texture image;

    private static final Texture[] images = new Texture[]{new Texture("asteroidPurple.png"),
        new Texture("asteroidGreen.png"), new Texture("asteroidRed.png")};

    public Asteroid() {
        position = new Vector2(0, 0);
        setSpeed();
        setScale();
        rotation = (float) (Math.random() * 360);
        hp = (int) (1 + Math.random() * 2);
        image = images[hp - 1];
    }

    public void update(float dt) {
        position.x -= (int) (speed + dt);
        if (position.x < -50 | hp == 0) {
            createAsteroid();
        }
    }

    public void conflict() {
        hp--;
        if (hp == 0) {
            destroy();
        }
    }

    private void setSpeed() {
        speed = (float) (5.f + Math.random() * Main.getCountAsteroidDestroy() / 100);
    }

    private void setScale() {
        scale = (float) (1.f + Math.random() * 1.5f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x, position.y, 32, 32, 64, 64, scale, scale, rotation, 0, 0, 64, 64, false, false);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void destroy() {
        Main.setCountAsteroidDestroy();
//        createAsteroid();
    }

    public void createAsteroid() {
        position.x = (float) (Gdx.graphics.getWidth() + 10 + Math.random() * 100);
        position.y = (float) SpaceShip.getSize()[1] / 2 + (Gdx.graphics.getHeight() - SpaceShip.getSize()[1]) * (float) Math.random();
        setSpeed();
        setScale();
        rotation = (float) (Math.random() * 360);
        hp = (int) (1 + Math.random() * 3);
        image = images[hp - 1];
    }
}
