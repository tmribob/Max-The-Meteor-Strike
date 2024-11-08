package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class SpaceShip {
    private final Vector2 position;
    private int speed;
    private float fireTime;
    private final float fireRate;
    private int hp;
    private boolean isDestroy;

    private static Texture shipImg;
    private final Texture[] shipImages = new Texture[]{
        new Texture("spaceShip/ship.png"),
        new Texture("spaceShip/damage1.png"),
        new Texture("spaceShip/damage2.png"),
        new Texture("spaceShip/damage3.png"),
        new Texture("spaceShip/broken.png")};


    SpaceShip() {
        fireRate = 0.2f;
        position = new Vector2();
        recoveryShip();
    }

    public void render(SpriteBatch batch) {
//        if (!isDestroy) {
//            batch.draw(shipImg, position.x - ((float) shipImg.getWidth() / 2), position.y - ((float) shipImg.getHeight() / 2));
//        } else {
//            batch.draw(shipImg, position.x - (float) shipImg.getWidth() / 2, position.y - (float) shipImg.getHeight() / 2, (float) shipImg.getWidth() / 2, (float) shipImg.getHeight() / 2,
//                shipImg.getWidth(), shipImg.getHeight(), 1, 1, -45, 0, 0, shipImg.getWidth(), shipImg.getHeight(), false, false);
//        }
        batch.draw(shipImg, position.x - ((float) shipImg.getWidth() / 2), position.y - ((float) shipImg.getHeight() / 2));

    }

    public void update(float dt, Sound laser) {
        if (isDestroy && position.y > -1 * ((float) shipImg.getHeight() / 2)) {
            position.x += speed * dt;
            position.y -= speed * dt;

        }
        if (!isDestroy) {
            if (Gdx.input.isKeyPressed(Input.Keys.D) | Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (position.x < Gdx.graphics.getWidth() - ((float) shipImg.getWidth() / 2))
                    position.x += speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) | Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (position.y < Gdx.graphics.getHeight() - ((float) shipImg.getHeight() / 2))
                    position.y += speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) | Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (position.x > (float) shipImg.getWidth() / 2)
                    position.x -= speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) | Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (position.y > (float) shipImg.getHeight() / 2)
                    position.y -= speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                fireTime += dt;
                if (fireTime > fireRate) {
                    fireTime -= fireRate;
                    long sound = laser.play(0.1f);
                    laser.setPitch(sound, MathUtils.random(0.8f, 1.2f));
                    fire();
                }
            }
        }
    }

    private void fire() {
        for (Bullet b : Main.getBullets()) {
            if (!b.isAvailable()) {
                b.setup(position);
                break;
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public static int[] getSize() {
        return new int[]{shipImg.getWidth(), shipImg.getHeight()};
    }

    public void getDamage(Sound destroy) {
        hp--;
        shipImg = shipImages[4 - hp];
        if (hp <= 2) {
            speed = 300;
        }
        if (hp == 0) {
            isDestroy = true;
            Main.setStatus("end");
            destroy.play(0.1f);
        }
    }

    public void recoveryShip() {
        hp = 4;
        speed = 400;
        shipImg = shipImages[4 - hp];
        position.set(shipImg.getWidth() + 30, (float) Gdx.graphics.getHeight() / 2);
        isDestroy = false;
    }

    public int getHp() {
        return hp;
    }
}
