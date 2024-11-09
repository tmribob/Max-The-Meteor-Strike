package MaxTheMeteorStrike.jdx.objects;

import MaxTheMeteorStrike.jdx.Main;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends CosmicObjects {

    private static final Texture[] images = new Texture[]{
            new Texture("asteroid/asteroidPurple.png"),
            new Texture("asteroid/asteroidGreen.png"),
            new Texture("asteroid/asteroidRed.png")};

    public Asteroid(byte hp) {
        super(hp, (short) 300, images[hp - 1], (float) (1.5f + Math.random() * hp / 2), (byte) MathUtils.random(-128, 127));
    }

    public void recreate() {
        hp = (byte) MathUtils.random(1, 3);
        image = images[hp - 1];
        setPosition();
        speed = (short) (350 + Math.random() * Main.getCountAsteroidDestroy() /2);
        scale = (float) (1.5f + Math.random() * hp / 2);
        rotation = (byte) MathUtils.random(-128, 127);
        height = image.getHeight();
        width = image.getWidth();
    }


    @Override
    public void destroy() {
        recreate();
    }

    public void conflict(Sound explode) {
        hp--;
        if (hp == 0) {
            destroy();
            explode.play(1.f);
            Main.setCountAsteroidDestroy();
        }
    }

}
