package MaxTheMeteorStrike.jdx;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class FireParticles {
    private static final int size = 2;
    private final Texture fireTex;
    private final Vector2 position;
    private float range;
    private static final int maxRange = 20;
    private static final int speed = 50;
    private boolean visible;

    FireParticles(){
        float[] color = new float[]{1, 0.68f, 0.32f};
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
        float indent = ((position.y + size[1]) - (position.y - (float) size[1] / 2)) / 6;
        this.position.set(position.x - (float) size[0] / 2, MathUtils.random(position.y - indent, position.y + indent));
        visible = true;
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose(){
        fireTex.dispose();
    }
}
