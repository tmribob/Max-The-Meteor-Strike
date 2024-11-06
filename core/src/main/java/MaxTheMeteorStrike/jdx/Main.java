package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

    @Override
    public void create() {
        batch = new SpriteBatch();
        stars = new Star[120];
        ship = new SpaceShip();
        bullets = new Bullet[40];
        fire = new FireParticles[100];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
        }
        for (int i = 0; i < fire.length; i++) {
            fire[i] = new FireParticles();
        }
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        for (int i = 0; i < MathUtils.random(0, fire.length); i++) {
            if (!fire[i].isVisible()) {
                fire[i].setPosition(ship.getPosition(), ship.getSize());
            }
        }
        update(dt);
        ScreenUtils.clear(0, 0, 0, 1f);
        batch.begin();
        for (Star star : stars) {
            star.render(batch);
        }
        ship.render(batch);
        for (Bullet b : bullets) {
            if (b.isAvailable()) {
                b.render(batch);
            }
        }
        for (FireParticles p : fire) {
            if (p.isVisible()) {
                p.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (FireParticles p : fire) {
            p.dispose();
        }
    }

    public void update(float dt) {
        ship.update(dt);
        for (Bullet b : bullets) {
            if (b.isAvailable()) {
                b.update(dt);
            }
        }
        for (FireParticles particle : fire) {
            if (particle.isVisible()) {
                particle.update(dt);
            }
        }
        for(Star star:stars){
            star.update(dt);
        }
    }

    public static Bullet[] getBullets() {
        return bullets;
    }
}
