package MaxTheMeteorStrike.jdx;

import com.badlogic.gdx.math.Vector2;

public class Asteroid {
    private final Vector2 position;
    private float speed;
    private float rotation;

    public Asteroid() {
        position = new Vector2(1024 * (float) Math.random(), 720 * (float) Math.random());
        speed = (float) (5.f + Math.random() * 5.f);
        rotation = (float) (Math.random() * 360);
    }
}
