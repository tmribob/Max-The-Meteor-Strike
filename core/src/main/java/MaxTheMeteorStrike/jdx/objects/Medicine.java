package MaxTheMeteorStrike.jdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Medicine {
    private final Vector2 position;
    private final float speed;
    private final Texture image;
    private boolean isHide;

    public Medicine() {
        image = new Texture("medicine.jpg");
        position = new Vector2(Gdx.graphics.getWidth() + 200,MathUtils.random((float) image.getHeight() / 2, Gdx.graphics.getHeight() - (float) image.getHeight() / 2));
        speed = 5.f;
        isHide = true;
    }
    public void update(float dt) {
        position.x -= (int) (speed + dt);
        if (position.x < -50 ) {
            isHide=true;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(image, position.x - (float) image.getWidth() / 2, position.y - (float) image.getHeight() / 2);
    }

    public void setHide(boolean isHide) {
        this.isHide = isHide;
        if (isHide){
            position.set(Gdx.graphics.getWidth() + 200,MathUtils.random((float) image.getHeight() / 2, Gdx.graphics.getHeight() - (float) image.getHeight() / 2));
        }
    }

    public boolean isHide() {
        return isHide;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getH(){
        return image.getHeight();
    }

    public float getW() {
        return image.getWidth();
    }
}
