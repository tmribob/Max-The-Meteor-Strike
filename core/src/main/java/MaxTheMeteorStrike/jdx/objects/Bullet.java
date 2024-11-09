package MaxTheMeteorStrike.jdx.objects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bullet extends CosmicObjects {
    private static final Texture bulletImg = new Texture("bullet.png");

    public Bullet() {
        super((byte) 1, (short) 800, bulletImg, 1, (byte) 0);
    }

    @Override
    public void update(float dt) {
        position.x += speed * dt;
        if (position.x > Gdx.graphics.getWidth() + width / 2) {
            destroy();
        }
    }

    public void setup(Vector2 position) {
        revive();
        this.position.set(position);

    }
}
