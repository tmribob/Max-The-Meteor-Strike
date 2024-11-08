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
    private static Texture shipImg;
    private final int speed;
    private float fireTime;
    private final float fireRate;
    private int hp;
    private final Texture[] shipImages = new Texture[]{
        new Texture("ship.png"),
        new Texture("damage1.png"),
        new Texture("damage2.png"),
        new Texture("damage3.png"),
        new Texture("broken.png")};


    SpaceShip() {
        hp = 5;
        getDamage(null);
        position = new Vector2(shipImg.getWidth() + 30, (float) Gdx.graphics.getHeight() / 2);
        speed = 400;
        fireRate = 0.2f;
    }

    public void render(SpriteBatch batch) {

        batch.draw(shipImg, position.x - ((float) shipImg.getWidth() / 2), position.y - ((float) shipImg.getHeight() / 2));
    }

    public void update(float dt, Sound laser) {
        if (hp == 0 && position.y > -1 * ((float) shipImg.getHeight() / 2)) {
            position.x += speed * dt;
            position.y -= speed * dt;

        }
        if (!Main.getStatus().equals("end")) {
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
        if (hp == 0) {
            destroy.play(0.1f);
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
        shipImg = shipImages[4 - hp];
    }

    public void setPosition() {
        position.set(shipImg.getWidth() + 30, (float) Gdx.graphics.getHeight() / 2);
    }
}
