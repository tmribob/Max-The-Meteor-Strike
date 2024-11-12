package MaxTheMeteorStrike.jdx;

import MaxTheMeteorStrike.jdx.objects.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.*;
import java.util.Scanner;


/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private static SpaceShip ship;
    private static Bullet[] bullets;
    private static FireParticles[] fire;
    private static Star[] stars;
    private static Asteroid[] asteroids;
    private static HealBox healBox;
    private static String status;
    private BitmapFont textField;
    private static int countAsteroidDestroy;
    private static int countAsteroid;
    private float[] opacity;
    private static int maxRecord;
    private static Sound laser;
    private static long soundLaser;
    private Sound destroy;
    private Music music;
    private Sound conflict;
    private Sound explode;
    private Sound heal;
    private OrthographicCamera camera;
    private Viewport viewport;


    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FillViewport(1280f, 720f, camera);
        Graphics.DisplayMode dm = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(dm);
        status = "start";
        textField = new BitmapFont();
        textField.getData().setScale(1.5f, 1.5f);
        batch = new SpriteBatch();
        ship = new SpaceShip();
        stars = new Star[200];
        bullets = new Bullet[20];
        fire = new FireParticles[100];
        asteroids = new Asteroid[50];
        countAsteroid = 5;
        healBox = new HealBox();
        opacity = new float[]{0.75f, 1.f};
        laser = Gdx.audio.newSound(Gdx.files.internal("audio/laser.mp3"));
        soundLaser = laser.play(0.1f);
        conflict = Gdx.audio.newSound(Gdx.files.internal("audio/conflict.mp3"));
        destroy = Gdx.audio.newSound(Gdx.files.internal("audio/destroy.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("audio/explode.mp3"));
        heal = Gdx.audio.newSound(Gdx.files.internal("audio/heal.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("audio/background.mp3"));
        music.play();
        music.setLooping(true);
        FireParticles.setColor();
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
            asteroids[i] = new Asteroid((byte) MathUtils.random(1, 3));
            if (i < countAsteroid) {
                asteroids[i].recreate();
            }
        }
        try (Scanner scanner = new Scanner(new File("records.txt"))) {
            maxRecord = scanner.nextInt();
        } catch (IOException e) {
            maxRecord = 0;
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyPressed(Input.Keys.F)){
            Graphics.DisplayMode dm = Gdx.graphics.getDisplayMode();
            Gdx.graphics.setFullscreenMode(dm);
            resize(dm.width, dm.height);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.graphics.setWindowedMode(1280, 720);
        ScreenUtils.clear(0, 0, 0, 1f);
        float dt = Gdx.graphics.getDeltaTime();
        update();
        batch.begin();
        for (Star star : stars) {
            star.update(dt);
            star.render(batch);
        }
        for (FireParticles particle : fire) {
            if (particle.isVisible()) {
                particle.render(batch);
                particle.update(dt);
            } else {
                particle.setPosition(ship.getPosition(), ship.getWidth(), ship.getHeight(), ship.getHp());
            }
        }
        ship.update(dt);
        ship.render(batch);
        if (status.equals("start")) {
            textField.draw(batch, "Press Enter for start game", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        }
        if (status.equals("playing")) {
            if (countAsteroidDestroy > maxRecord) {
                maxRecord = countAsteroidDestroy;
                writeRecord();
            }
            textField.draw(batch, "Asteroid destroyed: " + countAsteroidDestroy, (float) Gdx.graphics.getWidth() - 250, (float) Gdx.graphics.getHeight() - 25);
            textField.draw(batch, "Max record: " + maxRecord, (float) Gdx.graphics.getWidth() - 250, (float) Gdx.graphics.getHeight() - 50);
            for (Bullet bullet : bullets) {
                if (bullet.isActive()) {
                    bullet.update(dt);
                    bullet.render(batch);
                }
            }
            for (int i = 0; i < countAsteroid; i++) {
                asteroids[i].update(dt);
                if (ship.checkConflictWithShip(asteroids[i])) {
                    ship.getDamage(destroy);
                    conflict.play(1);
                    asteroids[i].recreate();
                    break;
                }
                for (Bullet bullet : bullets) {
                    if (bullet.isActive() && asteroids[i].checkConflictWithAsteroid(bullet, explode)) {
                        bullet.destroy();
                        break;
                    }
                }
                asteroids[i].render(batch);
            }
            if (healBox.isActive()) {
                healBox.update(dt);
                if (ship.checkConflictWithShip(healBox)) {
                    ship.heal();
                    heal.play(1);
                    healBox.destroy();
                }
                healBox.render(batch);
            } else if ((int) (Math.random() * 2500) == 252) {
                healBox.revive();
            }
        }
        batch.end();
    }

    public void update() {
        if (ship.getPosition().y < 0) {
            status = "start";
            ship.revive();
            countAsteroidDestroy = 0;
        }
        if (status.equals("start")) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                status = "playing";
                opacity[1] = 1.f;
                return;
            }
            if ((opacity[0] - 0.75f) * (opacity[0] - 0.25f) > 0) {
                opacity[1] = opacity[1] * (-1);
            }
            opacity[0] += 0.025f * opacity[1];
            textField.setColor(1, 1, 1, opacity[0]);
        }
    }


    public static void setCountAsteroidDestroy() {
        countAsteroidDestroy++;
        if (countAsteroidDestroy % 30 == 0) {
            countAsteroid++;
        }
    }

    public static int getCountAsteroidDestroy() {
        return countAsteroidDestroy;
    }

    private static void writeRecord() {
        try (FileWriter wr = new FileWriter("records.txt")) {
            wr.write("" + maxRecord);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void updateEnd() {
        status = "end";
        writeRecord();
        for (Bullet bullet : bullets) {
            bullet.destroy();
        }
        for (int i = 0; i < countAsteroid; i++) {
            asteroids[i].destroy();
        }
    }

    public static void fire() {
        if (status.equals("playing")) {
            for (Bullet bullet : bullets) {
                if (!bullet.isActive()) {
                    bullet.setup(ship.getPosition());
                    laser.play(0.1f);
                    laser.setPitch(soundLaser, MathUtils.random(0.8f, 1.2f));
                    break;
                }
            }
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        music.dispose();
        laser.dispose();
        destroy.dispose();
        heal.dispose();
        explode.dispose();
        conflict.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }
}

