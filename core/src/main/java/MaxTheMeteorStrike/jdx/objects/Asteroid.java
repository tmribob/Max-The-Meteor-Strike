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
        super(hp, 5.f, images[hp - 1], (float) (1.5f + Math.random() * hp / 2), (byte) MathUtils.random(-128, 127));
    }

    public void recreate() {
        hp = (byte) MathUtils.random(1, 3);
        image = images[hp - 1];
        setPosition();
        speed = (float) (5.f + Math.random() * Main.getCountAsteroidDestroy() / 100);
        scale = (float) (1.5f + Math.random() * hp / 2);
        rotation = (byte) MathUtils.random(-128, 127);
        height = image.getHeight();
        width = image.getWidth();
    }


    public void conflict(Sound explode) {
        hp--;
        if (hp == 0) {
            setActive(false);
            explode.play(1.f);
            Main.setCountAsteroidDestroy();
        }
    }

    @Override
    public void setActive(boolean active) {
        recreate();
    }

}