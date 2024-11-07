package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private SpaceShip ship;
    private static Bullet[] bullets;
    private static FireParticles[] fire;
    private Star[] stars;
    private Asteroid[] asteroids;
    private String status;
    private BitmapFont font;

    @Override
    public void create() {
        status = "start";
        font = new BitmapFont();
        batch = new SpriteBatch();
        stars = new Star[120];
        ship = new SpaceShip();
        bullets = new Bullet[40];
        fire = new FireParticles[100];
        asteroids = new Asteroid[2];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
        for (int i = 0; i < fire.length; i++) {
            fire[i] = new FireParticles();
        }
        for (int i = 0; i < asteroids.length; i++) {
            asteroids[i] = new Asteroid();
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1f);
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        for (int i = 0; i < MathUtils.random(0, fire.length); i++) {
            if (!fire[i].isVisible()) {
                fire[i].setPosition(ship.getPosition(), ship.getSize());
            }
        }
        batch.begin();
        for (Star star : stars) {
            star.render(batch);
        }

        ship.render(batch);
        for (FireParticles p : fire) {
            if (p.isVisible()) {
                p.render(batch);
            }
        }

        if (status.equals("start")) {
            font.draw(batch, "Press Enter for start game", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        }
        if (status.equals("start") && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            status = "playing";
        }
        if (status.equals("playing")) {
            for (Bullet b : bullets) {
                if (b.isAvailable()) {
                    b.render(batch);
                }
            }
            for (Asteroid asteroid : asteroids) {
                asteroid.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void update(float dt) {
        ship.update(dt);
        for (Star star : stars) {
            star.update(dt);
        }
        for (FireParticles particle : fire) {
            if (particle.isVisible()) {
                particle.update(dt);
            }
        }
        if (status.equals("playing")) {
            for (Bullet b : bullets) {
                if (b.isAvailable()) {
                    checkConflict(b);
                    b.update(dt);
                }
            }
            for (Asteroid asteroid : asteroids) {
                asteroid.update(dt, ship.getSize()[1]);
            }
        }
    }

    public static Bullet[] getBullets() {
        return bullets;
    }

    private void checkConflict(Bullet bullet) {
        for (Asteroid asteroid : asteroids) {
            if (bullet.getPosition().dst(asteroid.getPosition()) < 48) {
                bullet.setAvailable(false);
                asteroid.conflict();
                return;
            }
        }
    }
}
