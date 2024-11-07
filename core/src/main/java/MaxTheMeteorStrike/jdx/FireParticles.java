package MaxTheMeteorStrike.jdx;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class FireParticles {
    private static final int size = 4;
    private final Texture fireTex;
    private final Vector2 position;
    private float range; // на сколько далеко улетела частичка
    private static final int maxRange = 30; // максимальное расстояние, на которое может отлететь частичка
    private static final int speed = 90;
    private boolean visible; // активна частица или нет

    FireParticles(){
        float[] color = new float[]{1, 0.68f, 0.32f}; // цвет
        Pixmap fireParticle = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        fireParticle.setColor( color[0], color[1], color[2], 0.75f);
        fireParticle.fill();
        fireTex = new Texture(fireParticle);
        position = new Vector2(0, 0);
        visible = false;
    }

    public void render(SpriteBatch batch){
        batch.draw(fireTex, position.x, position.y);
    }

    public void update(float dt){
        if (visible) {
            position.x -= speed * dt;
            range += speed * dt;
            if (range >= maxRange) {
                visible = false;
                range = 0;
            }
        }
    }

    public void setPosition(Vector2 position, int[] size){
        this.position.set(position.x- (float) size[0] /2, MathUtils.random(position.y - (float) size[1] / 2, position.y + (float) size[1] / 2));
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

}
