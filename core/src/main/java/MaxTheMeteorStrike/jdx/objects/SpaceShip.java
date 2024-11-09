package MaxTheMeteorStrike.jdx.objects;

import MaxTheMeteorStrike.jdx.Main;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;


public class SpaceShip extends CosmicObjects {
    private float fireTime;
    private final float fireRate;
    private boolean isDestroy;

    private static final Texture[] shipImages = new Texture[]{
            new Texture("spaceShip/ship.png"),
            new Texture("spaceShip/damage1.png"),
            new Texture("spaceShip/damage2.png"),
            new Texture("spaceShip/damage3.png"),
            new Texture("spaceShip/broken.png")};

    public SpaceShip() {
        super((byte) 4, 400, shipImages[0], 1, (byte) 0);
        position.set(width + 30, (float) Gdx.graphics.getHeight() / 2);
        fireRate = 0.2f;
        isDestroy = false;
    }

    public void recoveryShip() {
        hp = 4;
        speed = 400;
        image = shipImages[4 - hp];
        position.set(width + 30, (float) Gdx.graphics.getHeight() / 2);
        isDestroy = false;
        rotation=0;
    }


    @Override
    public void update(float dt) {
        if (isDestroy && position.y > -1 * (height / 2)) {
            position.x += speed * dt;
            position.y -= speed * dt;
            if(rotation>-45){
                rotation--;
            }

        }
        if (!isDestroy) {
            if (Gdx.input.isKeyPressed(Input.Keys.D) | Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                if (position.x < Gdx.graphics.getWidth() - (width / 2))
                    position.x += speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) | Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if (position.y < Gdx.graphics.getHeight() - (height / 2))
                    position.y += speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) | Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (position.x > width / 2)
                    position.x -= speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) | Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if (position.y > height / 2)
                    position.y -= speed * dt;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                fireTime += dt;
                if (fireTime > fireRate) {
                    fireTime -= fireRate;
                    Main.fire();
                }
            }
        }
    }


    public void getDamage(Sound destroy) {
        hp--;
        image = shipImages[4 - hp];
        if (hp <= 2) {
            speed = 300;
        }
        if (hp == 0) {
            isDestroy = true;
            Main.ending();
            destroy.play(0.1f);
        }
    }

    public int getHp() {
        return hp;
    }

    public void heal() {
        if (hp < 4) {
            hp++;
            image = shipImages[4 - hp];
        }
    }
}
