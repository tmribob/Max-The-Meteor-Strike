package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class SpaceShip {
    private final Vector2 position;
    private final Texture shipImg;
    private final int speed;
    private float fireTime;
    private float fireRate;


    SpaceShip(){
        shipImg = new Texture("ship.png");
        position = new Vector2(shipImg.getHeight() + 30,(float) Gdx.graphics.getHeight() / 2);
        speed = 400;
        fireRate = 0.4f;
    }

    public void render(SpriteBatch batch){
        batch.draw(shipImg, position.x - ((float) shipImg.getWidth() / 2), position.y - ((float) shipImg.getHeight() / 2));
    }

    public void update(float dt){
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            if (position.x < Gdx.graphics.getWidth() - ((float) shipImg.getWidth() / 2))
                position.x += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            if (position.y < Gdx.graphics.getHeight() - ((float) shipImg.getHeight() / 2))
                position.y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            if (position.x > (float) shipImg.getWidth() / 2)
                position.x -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)){
            if (position.y > (float) shipImg.getHeight() / 2)
                position.y -= speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            fireTime += dt;
            if (fireTime > fireRate){
                fireTime -= fireRate;
                fire();
            }
        }
    }

    private void fire(){
        for (Bullet b : Main.getBullets()){
            if (!b.isAvailable()){
                b.setup(position);
                break;
            }
        }
    }
}
