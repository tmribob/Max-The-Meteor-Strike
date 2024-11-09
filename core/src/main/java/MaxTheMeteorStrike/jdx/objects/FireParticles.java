package MaxTheMeteorStrike.jdx.objects;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class FireParticles {
    private static final int size = 3;
    private static Texture[] fireTex;
    private final Vector2 position;
    private float range; // на сколько далеко улетела частичка
    private static final int maxRange = 30; // максимальное расстояние, на которое может отлететь частичка
    private static final int speed = 90;
    private boolean visible; // активна частица или нет
    private int count;

    public static void setColor() {
        Pixmap fireParticle1 = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        fireParticle1.setColor(1, 0.41f, 0.23f, 0.75f);
        fireParticle1.fill();
        Pixmap fireParticle2 = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        fireParticle2.setColor(1, 0.68f, 0.32f, 0.75f);
        fireParticle2.fill();
        Pixmap fireParticle3 = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        fireParticle3.setColor(1, 0.9f, 0.43f, 0.75f);
        fireParticle3.fill();

        fireTex = new Texture[]{new Texture(fireParticle1), new Texture(fireParticle2), new Texture(fireParticle3)};
    }

    public FireParticles() {
        position = new Vector2(0, 0);
        visible = false;
        count = 0;
    }

    public void render(SpriteBatch batch) {
        batch.draw(fireTex[count], position.x, position.y);
    }

    public void update(float dt) {
        position.x -= speed * dt;
        if (range / maxRange < (float) 1 / 3) {
            count = 0;
        } else if (range / maxRange < (float) 1 / 2) {
            count = 1;
        } else {
            count = 2;
        }
        range += speed * dt;
        if (range >= maxRange * (1 + Math.random())) {
            visible = false;
            range = 0;
        }

    }

    public void setPosition(Vector2 position, float width,float height, int hp) {
        visible = true;
        if (hp == 2 || hp == 1) {
            this.position.set(position.x - width / 2, MathUtils.random(position.y + height / 6, position.y + height / 2));
            return;
        }
        float range1 = MathUtils.random(position.y - height / 2, position.y - height / 6);
        float range2 = MathUtils.random(position.y + height / 6, position.y + height / 2);
        this.position.set(position.x - width / 2, Math.random() * 2 < 1 ? range1 : range2);
    }

    public boolean isVisible() {
        return visible;
    }

}
