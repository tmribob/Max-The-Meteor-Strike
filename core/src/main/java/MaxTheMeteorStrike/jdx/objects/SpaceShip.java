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
        super((byte) 4, (short) 400, shipImages[0], 1, (byte) 0);
        position.set(width + 30, (float) Gdx.graphics.getHeight() / 2);
        fireRate = 0.2f;
        isDestroy = false;
    }

    @Override
    public void revive() {
        hp = 4;
        speed = (short) 400;
        image = shipImages[4 - hp];
        position.set(width + 30, (float) Gdx.graphics.getHeight() / 2);
        isDestroy = false;
        rotation = 0;
    }

    @Override
    public void update(float dt) {
        if (isDestroy && position.y > -1 * (height / 2)) {
            position.x += speed * dt;
            position.y -= speed * dt;
            if (rotation > -45) {
                rotation--;
            }
            return;
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
                if (fireTime < 0) {
                    Main.fire();
                    fireTime = fireRate;
                }
            }
            if (fireTime >= 0) {
                fireTime -= dt;
            }
        }
    }


    public void getDamage(Sound destroy) {
        hp--;
        image = shipImages[4 - hp];
        if (hp == 0) {
            isDestroy = true;
            Main.updateEnd();
            destroy.play(0.1f);
            return;
        }
        if (hp <= 2) {
            speed = (short) 300;
        }
    }

    public int getHp() {
        return hp;
    }

    public void heal() {
        if (hp < 4) {
            hp++;
            image = shipImages[4 - hp];
            if(hp>2){
                speed = (short) 400;
            }
        }
    }

    public boolean checkConflictWithShip(CosmicObjects object) {
        return Math.pow((object.getPosition().x - position.x) / (object.getWidth() * 2 + width), 2)
                + Math.pow((object.getPosition().y - position.y) / (object.getHeight() * 2 + height), 2)
                <= 0.25f;
    }
}
