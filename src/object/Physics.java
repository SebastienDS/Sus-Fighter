package object;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

public class Physics {

    public static Body addBodyStatic(World world, float x, float y, float width, float height) {
        var groundBody = new BodyDef();
        groundBody.position.set(new Vec2(x, y));
        var body = world.createBody(groundBody);
        var polygon = new PolygonShape();
        polygon.setAsBox(width, height);
        body.createFixture(polygon, 0.0f);
        return body;
    }

    public static Body addDynamicBody(World world, float x, float y, float width,
                                       float height, float density, float friction) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(x, y);
        var body = world.createBody(bodyDef);
        PolygonShape dynamicBox = new PolygonShape();
        dynamicBox.setAsBox(width, height);
        var fixtureDef = new FixtureDef();
        fixtureDef.shape = dynamicBox;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        body.createFixture(fixtureDef);
        return body;
    }
}
