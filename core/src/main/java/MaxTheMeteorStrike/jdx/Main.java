package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


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
    private static String  status;
    private BitmapFont textField;
    private static int countAsteroidDestroy;
    private float[] opacity;
    private static int maxRecord;
    private Sound laser;
    private Sound destroy;
    private Music music;

    @Override
    public void create() {
        status = "start";
        textField = new BitmapFont();
        textField.getData().setScale(1.5f, 1.5f);
        batch = new SpriteBatch();
        stars = new Star[170];
        ship = new SpaceShip();
        bullets = new Bullet[40];
        fire = new FireParticles[100];
        asteroids = new Asteroid[50];
        opacity = new float[]{0.75f, 1.f};
        music = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        music.play();
        music.setLooping(true);
        laser = Gdx.audio.newSound(Gdx.files.internal("laser.mp3"));
        destroy = Gdx.audio.newSound(Gdx.files.internal("destroy.mp3"));
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
        try (FileInputStream fileIn = new FileInputStream("records.txt")) {
            maxRecord= fileIn.read();
        } catch (IOException e) {
            maxRecord=0;
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1f);
        float dt = Gdx.graphics.getDeltaTime();
        if (ship.getPosition().y < 0){
            status="start";
            ship.setHp(4);
            ship.setPosition();
            writeRecord();
            for (int i = 0; i < 5 + countAsteroidDestroy / 30; i++){
                asteroids[i].createAsteroid();
            }
        }
        update(dt);
        if (ship.getHp() == 0){
            status="end";
            countAsteroidDestroy=0;
            for (Bullet b : bullets){
                b.setAvailable(false);
            }
        }
        for (int i = 0; i < MathUtils.random(0, fire.length); i++) {
            if (!fire[i].isVisible()) {
                fire[i].setPosition(ship.getPosition(), SpaceShip.getSize());
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

            textField.draw(batch, "Press Enter for start game", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                status = "playing";
                opacity[1] = 1.f;
            }
        }
        if (status.equals("playing")) {
            if(countAsteroidDestroy>maxRecord){
                maxRecord=countAsteroidDestroy;
            }
            textField.draw(batch, "Asteroid destroyed: " + countAsteroidDestroy, (float) Gdx.graphics.getWidth() - 250, (float) Gdx.graphics.getHeight() - 25);
            textField.draw(batch, "Max record: " + maxRecord, (float) Gdx.graphics.getWidth() - 250, (float) Gdx.graphics.getHeight() - 50);
            for (Bullet b : bullets) {
                if (b.isAvailable()) {
                    b.render(batch);
                }
            }
            for (int i = 0; i < 5 + countAsteroidDestroy / 30; i++) {
                asteroids[i].render(batch);
            }
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
        laser.dispose();
        destroy.dispose();
    }

    public void update(float dt) {
        ship.update(dt,laser);
        for (Star star : stars) {
            star.update(dt);
        }
        for (FireParticles particle : fire) {
            if (particle.isVisible()) {
                particle.update(dt);
            }
        }
        if (status.equals("start")) {
            updateStart();
        }
        if (status.equals("playing")) {
            for (int i = 0; i < 5 + countAsteroidDestroy / 30; i++) {
                checkConflict(asteroids[i]);
                asteroids[i].update(dt);
            }
            for (Bullet b : bullets) {
                if (b.isAvailable()) {
                    b.update(dt);
                }
            }
        }
    }

    public void updateStart() {
        if (opacity[1] == 1.f) {
            opacity[0] -= 0.025f;
        } else {
            opacity[0] += 0.025f;
        }
        if (opacity[0] > 0.75f) {
            opacity[1] = 1.f;
        }
        if (opacity[0] < 0.25f) {
            opacity[1] = 0.f;
        }
        textField.setColor(1, 1, 1, opacity[0]);
    }

    public static Bullet[] getBullets() {
        return bullets;
    }

    private void checkConflict(Asteroid asteroid) {
        for (Bullet bullet : bullets) {
            if (!bullet.isAvailable()){
                continue;
            }
            if ((bullet.getPosition().x + (float) bullet.getW() / 2 >= asteroid.getPosition().x - (float) asteroid.getW() / 2 ) &&
                (Math.abs((asteroid.getPosition().y + (float) asteroid.getH() / 2) - (bullet.getPosition().y + (float) bullet.getH() / 2))
                    < (float) asteroid.getH())){
                bullet.setAvailable(false);
                asteroid.conflict();
                return;
            }
        }
        if ((asteroid.getPosition().x <= ship.getPosition().x + (float) SpaceShip.getSize()[0] /2)
                && ((Math.abs((asteroid.getPosition().y + (float) asteroid.getH() / 2) - (ship.getPosition().y + (float) SpaceShip.getSize()[1] / 2)))
                <= (float) SpaceShip.getSize()[1] / 2)){

            ship.getDamage(destroy);
            asteroid.createAsteroid();
        }

    }

    public static void setCountAsteroidDestroy() {
        countAsteroidDestroy++;
    }

    public static int getCountAsteroidDestroy() {
        return countAsteroidDestroy;
    }

    public static String getStatus(){
        return status;
    }

    private static void writeRecord(){
        try (FileOutputStream fileOut = new FileOutputStream("records.txt")) {
            fileOut.write(maxRecord);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

