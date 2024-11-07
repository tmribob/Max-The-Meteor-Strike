package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private final Vector2 position;
    private float speed;
    private float rotation;
    private float scale;
    private int hp;
    private Texture image;

    private static final Texture[] images = new Texture[]{
        new Texture("asteroidPurple.png"),
        new Texture("asteroidGreen.png"),
        new Texture("asteroidRed.png")};

    public Asteroid() {
        position = new Vector2();
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
        batch.draw(image, position.x - (float) image.getWidth() / 2, position.y - (float) image.getHeight() / 2, (float) image.getWidth() / 2, (float) image.getHeight() / 2,
                    image.getWidth(), image.getHeight(), scale, scale, rotation, 0, 0, image.getWidth(), image.getHeight(), false, false);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void destroy() {
        Main.setCountAsteroidDestroy();
    }

    public void createAsteroid() {
        hp = (int) (1 + Math.random() * 3);
        image = images[hp - 1];
        position.x = MathUtils.random(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() + 200);
        position.y = MathUtils.random((float) image.getHeight() / 2, Gdx.graphics.getHeight() - (float) image.getHeight() / 2);
        setSpeed();
        setScale();
        rotation = (float) (Math.random() * 360);


    }

    public int getW(){
        return image.getWidth();
    }

    public int getH(){
        return image.getHeight();
    }


}
