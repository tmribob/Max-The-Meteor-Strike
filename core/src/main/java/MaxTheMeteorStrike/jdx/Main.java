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
    private static Medicine medicine;
    private static String status;
    private BitmapFont textField;
    private static int countAsteroidDestroy;
    private float[] opacity;
    private static int maxRecord;
    private static Sound laser;
    private static long soundLaser;
    private Sound destroy;
    private Music music;
    private Sound conflict;
    private Sound explode;
    private Sound heal;

    @Override
    public void create() {
        status = "start";
        textField = new BitmapFont();
        textField.getData().setScale(1.5f, 1.5f);
        batch = new SpriteBatch();
        ship = new SpaceShip();
        stars = new Star[170];
        bullets = new Bullet[40];
        fire = new FireParticles[100];
        asteroids = new Asteroid[50];
        medicine = new Medicine();
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
            asteroids[i] = new Asteroid();
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
        ScreenUtils.clear(0, 0, 0, 1f);
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        batch.begin();
        for (Star star : stars) {
            star.render(batch);
        }
        for (FireParticles p : fire) {
            if (p.isVisible()) {
                p.render(batch);
            }
        }
        ship.render(batch);
        if (status.equals("start")) {
            textField.draw(batch, "Press Enter for start game", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        }
        if (status.equals("playing")) {
            if (countAsteroidDestroy > maxRecord) {
                maxRecord = countAsteroidDestroy;
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
            if (!medicine.isHide()) {
                medicine.render(batch);
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
        heal.dispose();
        explode.dispose();
        conflict.dispose();
    }

    public void update(float dt) {
        if (ship.getPosition().y < 0) {
            status = "start";
            ship.recoveryShip();
            countAsteroidDestroy = 0;
        }
        ship.update(dt);
        for (Star star : stars) {
            star.update(dt);
        }
        for (FireParticles particle : fire) {
            if (particle.isVisible()) {
                particle.update(dt);
            } else {
                particle.setPosition(ship.getPosition(), SpaceShip.getSize(), ship.getHp());
            }
        }
        if (status.equals("start")) {
            updateOpacity();
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
            if (!medicine.isHide()) {
                medicine.update(dt);
                checkMedicine();
            } else if ((int) (Math.random() * 5000) == 252) {
                medicine.setHide(false);
            }
        }
    }

    public void updateOpacity() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            status = "playing";
            opacity[1] = 1.f;
            return;
        }
        if (opacity[0] * (opacity[0] - 1) + (float) 3 / 16 > 0) {
            opacity[1] = opacity[1] * (-1);
        }
        opacity[0] += 0.025f * opacity[1];
        textField.setColor(1, 1, 1, opacity[0]);
    }


    private void checkConflict(Asteroid asteroid) {
        for (Bullet bullet : bullets) {
            if (!bullet.isAvailable()) {
                continue;
            }
            if ((bullet.getPosition().x + (float) bullet.getW() / 2 >= asteroid.getPosition().x - asteroid.getW() / 2) &&
                (Math.abs((asteroid.getPosition().y + asteroid.getH() / 2) - (bullet.getPosition().y + (float) bullet.getH() / 2))
                    < asteroid.getH())) {
                bullet.setAvailable(false);
                asteroid.conflict(explode);
                return;
            }
        }
        if (Math.pow((asteroid.getPosition().x - ship.getPosition().x) / (asteroid.getW() * 1.5f + SpaceShip.getSize()[0]), 2)
            + Math.pow((asteroid.getPosition().y - ship.getPosition().y) / (asteroid.getH() * 1.5f + SpaceShip.getSize()[1]), 2)
            <= 0.25f) {
            ship.getDamage(destroy);
            conflict.play(1);
            asteroid.createAsteroid();
        }

    }

    public void checkMedicine() {
        if (Math.pow((medicine.getPosition().x - ship.getPosition().x) / (medicine.getW() * 1.5f + SpaceShip.getSize()[0]), 2)
            + Math.pow((medicine.getPosition().y - ship.getPosition().y) / (medicine.getH() * 1.5f + SpaceShip.getSize()[1]), 2)
            <= 0.25f) {
            ship.heal();
            heal.play(1);
            medicine.setHide(true);
        }
    }

    public static void setCountAsteroidDestroy() {
        countAsteroidDestroy++;
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

    public static void ending() {
        status = "end";
        writeRecord();
        for (Bullet b : bullets) {
            b.setAvailable(false);
        }
        for (int i = 0; i < 5 + countAsteroidDestroy / 30; i++) {
            asteroids[i].createAsteroid();
        }
    }

    public static void fire() {
        if (status.equals("playing")) {
            for (Bullet b : bullets) {
                if (!b.isAvailable()) {
                    b.setup(ship.getPosition());
                    laser.play(0.1f);
                    laser.setPitch(soundLaser, MathUtils.random(0.8f, 1.2f));
                    break;
                }
            }
        }
    }
}

