package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private SpaceShip ship;
    private static Bullet[] bullets;

    @Override
    public void create() {
        batch = new SpriteBatch();
        ship = new SpaceShip();
        bullets = new Bullet[100];
        for (int i = 0; i < bullets.length; i++){
            bullets[i] = new Bullet();
        }
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        ScreenUtils.clear(0.23f, 0.05f, 0.41f, 1f);
        batch.begin();
        ship.render(batch);
        for (Bullet b : bullets){
            if (b.isAvailable()){
                b.render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void update(float dt){
        ship.update(dt);
        for (Bullet b : bullets){
            if (b.isAvailable()){
                b.update(dt);
            }
        }
    }

    public static Bullet[] getBullets() {
        return bullets;
    }
}
