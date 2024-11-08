package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
        new Texture("asteroid/asteroidPurple.png"),
        new Texture("asteroid/asteroidGreen.png"),
        new Texture("asteroid/asteroidRed.png")};

    public Asteroid() {
        position = new Vector2(-100,-100);
        createAsteroid();
    }

    public void createAsteroid() {
        hp = MathUtils.random(1,3);
        image = images[hp - 1];
        position.x = MathUtils.random(Gdx.graphics.getWidth(), Gdx.graphics.getWidth() + 200);
        position.y = MathUtils.random((float) image.getHeight() / 2, Gdx.graphics.getHeight() - (float) image.getHeight() / 2);
        speed = (float) (5.f + Math.random() * Main.getCountAsteroidDestroy() / 100);
        scale = (float) (1.5f + Math.random() * hp/2);
        rotation = (float) (Math.random() * 360);
    }

    public void update(float dt) {
        position.x -= (int) (speed + dt);
        if (position.x < -50 || hp == 0) {
            createAsteroid();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x - (float) image.getWidth() / 2, position.y - (float) image.getHeight() / 2, (float) image.getWidth() / 2, (float) image.getHeight() / 2,
                    image.getWidth(), image.getHeight(), scale, scale, rotation, 0, 0, image.getWidth(), image.getHeight(), false, false);
    }

    public void conflict(Sound explode) {
        hp--;
        if (hp == 0) {
            explode.play(1.f);
            Main.setCountAsteroidDestroy();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getW(){
        return image.getWidth();
    }

    public float getH(){
        return image.getHeight();
    }


}
