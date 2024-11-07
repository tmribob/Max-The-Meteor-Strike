package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private final Vector2 position;
    private static final Texture bulletImg = new Texture("bullet.png");
    private static int speed;
    private boolean available;

    Bullet(){
        position = new Vector2(0, Gdx.graphics.getHeight());
        speed = 800;
        available = false;
    }

    public void render(SpriteBatch batch){
        batch.draw(bulletImg, position.x - ((float) bulletImg.getWidth() / 2), position.y - ((float) bulletImg.getHeight() / 2));
    }

    public void update(float dt){
        if (available) {
            position.x += speed * dt;
            if (position.x + (float) bulletImg.getWidth() / 2 >= Gdx.graphics.getWidth()) {
                available = false;
            }
        }
    }

    public void setup(Vector2 position) {
        this.position.set(position);
        available = true;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getW(){
        return bulletImg.getWidth();
    }

    public int getH(){
        return bulletImg.getHeight();
    }
}
