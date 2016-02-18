/*import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.badlogic.gdx.Input.Keys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.MappedByteBuffer;
import java.nio.charset.Charset;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class gdxGLES2 implements InputProcessor, ApplicationListener {

    BitmapFont font;

    SpriteBatch spriteBatch;

	Matrix4 projection = new Matrix4();
	Matrix4 view = new Matrix4();
    Matrix4 pv = new Matrix4();
	Matrix4 model = new Matrix4();
	Matrix4 combined = new Matrix4();
    private static Matrix4 tempM = new Matrix4();     // general purpose use

    
    ShaderProgram shader;
    Texture texture1,texture2;
    Mesh mesh1,mesh2;

    float ang=0,xang=0,yang=0;
    int lx,ly;
    int winWidth,winHeight;
    float winAspect;
    private Vector3 lightPos = new Vector3(150,50,50);
    float lightAng;
    
    // this should be in a platform stub class....
    public static void main(String[] args) {
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;
        config.title = "gdx GLES2.0";
        config.useGL20 = true;
        config.forceExit = false; 
        new LwjglApplication(new gdxGLES2(), config);

    }


    @Override public void render () {
        
        ang += Gdx.graphics.getDeltaTime()*36f;
        lightAng += 1.5*Gdx.graphics.getDeltaTime();
        lightPos.x=(float)Math.sin(lightAng)*100.0f;
        lightPos.z=(float)Math.cos(lightAng)*100.0f;
        
        Gdx.gl20.glViewport(0, 0, winWidth, winHeight);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        Gdx.gl20.glEnable(GL20.GL_CULL_FACE);


        shader.begin();
        shader.setUniformi("s_texture1", 0); // first texture unit (0)
        shader.setUniformf("u_lightPos", lightPos );

		projection.setToProjection(0.1f, 1000.0f, 67.0f, winAspect);
		view.idt().trn(0, 0, -4f);
        pv.set(projection).mul(view);


        // sphere
        model.idt().trn(-1.5f,0,0).rotate(1,0,0,xang).rotate(0,1,0,yang);
        combined.set(pv).mul(model);
        shader.setUniformMatrix("u_mvp_mat", combined);

        tempM.set(view).mul(model);
        shader.setUniformMatrix("u_mv_mat",tempM);

        shader.setUniformMatrix("u_v_mat",view);

        //normal matrix is the inverse transpose of the model-view matrix to account for non uniform scaling...
        // Although we're not scaling in this simple example, its still worth leaving in...
        tempM.set(view).mul(model).tra().inv();
        shader.setUniformMatrix("u_norm_mat",tempM);
            
        texture1.bind();
        mesh1.render(shader, GL20.GL_TRIANGLES);


        // torus
        model.idt().trn(1.5f,0,0).rotate(1,0,0,ang).rotate(0,1,0,-ang*1.2f);
        combined.set(pv).mul(model);
        shader.setUniformMatrix("u_mvp_mat", combined);

        tempM.set(view).mul(model);
        shader.setUniformMatrix("u_mv_mat",tempM);

        shader.setUniformMatrix("u_v_mat",view);

        tempM.set(view).mul(model).tra().inv();
        shader.setUniformMatrix("u_norm_mat",tempM);

        texture2.bind();
        mesh2.render(shader, GL20.GL_TRIANGLES);

        shader.end();
        
        Gdx.gl20.glDisable(GL20.GL_CULL_FACE);
        spriteBatch.begin();
        font.draw(spriteBatch, "Frames Per Second: " + Gdx.graphics.getFramesPerSecond(), 30, 30);
        spriteBatch.end();

    }

    @Override public void create () {
        
        Gdx.input.setInputProcessor(this);

        winWidth = Gdx.graphics.getWidth();
        winHeight = Gdx.graphics.getHeight();
        winAspect = (float)winWidth/(float)winHeight;
                
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        
        texture1 = new Texture(Gdx.files.internal("data/day.png"),true);
        texture2 = new Texture(Gdx.files.internal("data/logo.png"),true);

        String vertexShader = readFile("data/texture.vert");
        String fragmentShader = readFile("data/texture.frag");
        shader = new ShaderProgram(vertexShader, fragmentShader);
        if (!shader.isCompiled()) {
            Gdx.app.log("Shader error!",shader.getLog());
        }
                
        mesh1 = ObjLoader.loadObjFromString(readFile("data/sphere.obj"),true);
        mesh1.getVertexAttribute(Usage.Position).alias = "a_position"; 
        mesh1.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texCoord"; 
        
        mesh2 = ObjLoader.loadObjFromString(readFile("data/torus.obj"),true);
        mesh2.getVertexAttribute(Usage.Position).alias = "a_position"; 
        mesh2.getVertexAttribute(Usage.TextureCoordinates).alias = "a_texCoord"; 

        Gdx.gl20.glEnable(GL20.GL_DEPTH_TEST);

    }

    @Override public void resize (int width, int height) {
        winWidth = width;
        winHeight = height;
        winAspect = (float)winWidth / (float)winHeight;
        Gdx.gl20.glViewport(0, 0, winWidth, winHeight);

    }


    @Override public boolean keyDown (int keycode) {
        if (keycode==Keys.ESCAPE) Gdx.app.exit();
        return false;

    }

    @Override public void dispose () {
        texture1.dispose();
        texture2.dispose();
        shader.dispose();
        mesh1.dispose();
        mesh2.dispose();
    }

    @Override public boolean touchDown (int x, int y, int pointer, int newParam) {
        lx=x;ly=y;
        return false;
 
    }
    
    @Override public boolean touchDragged (int x, int y, int pointer) {
        yang+=((x-lx)*Gdx.graphics.getDeltaTime())*24f;
        xang+=((y-ly)*Gdx.graphics.getDeltaTime())*24f;
        lx=x;ly=y;
        return false;

    }

    @Override public void resume() { // TODO reload textures here - TODO if possible check to see if this is needed?
    }


    @Override public boolean scrolled (int amount) { return false; }
    @Override public boolean keyUp (int keycode) { return false; }
    @Override public boolean keyTyped (char character) { return false; }
    @Override public boolean touchUp (int x, int y, int pointer, int button) { return false; }
    @Override public void pause() {  }

    private static String readFile(String path) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println("could not find "+path);
        }
        if (stream!=null) {
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                 Instead of using default, pass in a decoder. 
                return Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return "";

    }


}*/
