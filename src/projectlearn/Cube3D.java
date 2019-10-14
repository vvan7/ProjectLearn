/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectlearn;

import java.util.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *
 * @author Viet
 */
public class Cube3D extends Application {
    
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    
    private Rotate rotation;
    private double speed = 300;
    
    private CubeModel cube = new CubeModel();
    boolean shuffling = false, solving = false;
    private int shuffles = 0;
    private Random rand = new Random();
    
    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Group cubeGroup = new Group();
        Scene scene = new Scene(root, 500, 500, true);
        
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateX(-250.0);
        camera.setTranslateY(-250.0);
        scene.setCamera(camera);
        
        //https://gist.github.com/jperedadnr/28534fcdce605b75382b
        //code for mouse click drag rotation
        Rotate rotateX = new Rotate(20, 0, 0, 0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(-20, 0, 0, 0, Rotate.Y_AXIS);
        cubeGroup.getTransforms().addAll(rotateX, rotateY);
        
        root.getChildren().addAll(cubeGroup, new AmbientLight(Color.WHITE));

        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });
        //
        
        Group[][][] cubeletGroup = new Group[3][3][3];
        createCube3D(cubeletGroup);
        addCubelets(cubeGroup, cubeletGroup);
        
        shuffleCube(cubeGroup, cubeletGroup);
        
        primaryStage.setTitle("application"); 
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void createCube3D(Group[][][] cubeletGroup) {
        PhongMaterial[] colorMat = new PhongMaterial[6];
        colorMat[0] = new PhongMaterial(Color.GREEN);
        colorMat[1] = new PhongMaterial(Color.BLUE);
        colorMat[2] = new PhongMaterial(Color.WHITE);
        colorMat[3] = new PhongMaterial(Color.YELLOW);
        colorMat[4] = new PhongMaterial(Color.ORANGE);
        colorMat[5] = new PhongMaterial(Color.RED);
        
        initializeCubeletGroup(cubeletGroup);
        double cubic = 50.0, square = 43.0, thick = 2.0;
        Box[][][] cubelets = new Box[3][3][3];  //Create black boxes
        Box[][] faceColors = new Box[6][9];     //Create color faces
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.BLACK);
        for(int z = 0; z < 3; z++) {
            for(int y = 0; y < 3; y++) {
                for( int x = 0; x < 3; x++) {
                    cubelets[z][y][x] = new Box(cubic, cubic, cubic);
                    cubelets[z][y][x].setMaterial(material);
                    cubelets[z][y][x].setTranslateX(cubic * x - cubic);
                    cubelets[z][y][x].setTranslateY(cubic * y - cubic);
                    cubelets[z][y][x].setTranslateZ(cubic * z - cubic);
                    cubeletGroup[z][y][x].getChildren().add(cubelets[z][y][x]);
                    
                    int n = (y + 1) * (x + 1) - 1;
                    if(z == 0) {    //Face G
                        faceColors[0][n] = new Box(square, square, thick);
                        faceColors[0][n].setMaterial(colorMat[cube.getCubelet(z, y, x).getF() - 1]);
                        faceColors[0][n].setTranslateX(cubelets[z][y][x].getTranslateX());
                        faceColors[0][n].setTranslateY(cubelets[z][y][x].getTranslateY());
                        faceColors[0][n].setTranslateZ(cubelets[z][y][x].getTranslateZ() - cubic / 2);
                        cubeletGroup[z][y][x].getChildren().add(faceColors[0][n]);
                    }
                    if(z == 2) {    //Back B
                        faceColors[1][n] = new Box(square, square, thick);
                        faceColors[1][n].setMaterial(colorMat[cube.getCubelet(z, y, x).getB() - 1]);
                        faceColors[1][n].setTranslateX(cubelets[z][y][x].getTranslateX());
                        faceColors[1][n].setTranslateY(cubelets[z][y][x].getTranslateY());
                        faceColors[1][n].setTranslateZ(cubelets[z][y][x].getTranslateZ() + cubic / 2);
                        cubeletGroup[z][y][x].getChildren().add(faceColors[1][n]);
                    }
                    if(y == 0) {    //Up W
                        faceColors[2][n] = new Box(square, thick, square);
                        faceColors[2][n].setMaterial(colorMat[cube.getCubelet(z, y, x).getU() - 1]);
                        faceColors[2][n].setTranslateX(cubelets[z][y][x].getTranslateX());
                        faceColors[2][n].setTranslateY(cubelets[z][y][x].getTranslateY() - cubic / 2);
                        faceColors[2][n].setTranslateZ(cubelets[z][y][x].getTranslateZ());
                        cubeletGroup[z][y][x].getChildren().add(faceColors[2][n]);
                    }
                    if(y == 2) {    //Down Y
                        faceColors[3][n] = new Box(square, thick, square);
                        faceColors[3][n].setMaterial(colorMat[cube.getCubelet(z, y, x).getD() - 1]);
                        faceColors[3][n].setTranslateX(cubelets[z][y][x].getTranslateX());
                        faceColors[3][n].setTranslateY(cubelets[z][y][x].getTranslateY() + cubic / 2);
                        faceColors[3][n].setTranslateZ(cubelets[z][y][x].getTranslateZ());
                        cubeletGroup[z][y][x].getChildren().add(faceColors[3][n]);
                    }
                    if(x == 0) {    //Left O
                        faceColors[4][n] = new Box(thick, square, square);
                        faceColors[4][n].setMaterial(colorMat[cube.getCubelet(z, y, x).getL() - 1]);
                        faceColors[4][n].setTranslateX(cubelets[z][y][x].getTranslateX() - cubic / 2);
                        faceColors[4][n].setTranslateY(cubelets[z][y][x].getTranslateY());
                        faceColors[4][n].setTranslateZ(cubelets[z][y][x].getTranslateZ());
                        cubeletGroup[z][y][x].getChildren().add(faceColors[4][n]);
                    }
                    if(x == 2) {    //Right R
                        faceColors[5][n] = new Box(thick, square, square);
                        faceColors[5][n].setMaterial(colorMat[cube.getCubelet(z, y, x).getR() - 1]);
                        faceColors[5][n].setTranslateX(cubelets[z][y][x].getTranslateX() + cubic / 2);
                        faceColors[5][n].setTranslateY(cubelets[z][y][x].getTranslateY());
                        faceColors[5][n].setTranslateZ(cubelets[z][y][x].getTranslateZ());
                        cubeletGroup[z][y][x].getChildren().add(faceColors[5][n]);
                    }
                }
            }
        }
    }
    
    public void addCubelets(Group cubeGroup, Group[][][] cubeletGroup) {
        for(int z = 0; z < 3; z++) {
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    cubeGroup.getChildren().add(cubeletGroup[z][y][x]);
                }
            }
        }
    }
    
    public void initializeCubeletGroup(Group[][][] cubeletGroup) {
        for(int z = 0; z < 3; z++) {
            for(int y = 0; y < 3; y++) {
                for(int x = 0; x < 3; x++) {
                    cubeletGroup[z][y][x] = new Group();
                }
            }
        }
    }
    
    public void updateCube3D(Group cubeGroup, Group[][][] cubeletGroup) {
        cubeGroup.getChildren().clear();
        createCube3D(cubeletGroup);
        addCubelets(cubeGroup, cubeletGroup);
    }
    
    public void addTimeline(Group cubeGroup, Group[][][] cubeletGroup, double angle) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), 0.0)),
                new KeyFrame(Duration.millis(speed), new KeyValue(rotation.angleProperty(), angle)));
        timeline.setOnFinished(event -> {
            updateCube3D(cubeGroup, cubeletGroup);
            if(shuffling) {
                if(shuffles < 100) {
                    shuffles++;
                    //random rotation
                    pickRotation(cubeGroup, cubeletGroup, rand.nextInt(18));
                }
                else {
                    shuffling = false;
                }
            }
        });
        timeline.play();
    }
    
    public void addRotateGroupX(Group[][][] cubeletGroup, int x) {
        rotation = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cubeletGroup[i][j][x].getTransforms().add(rotation);
            }
        }
    }
    
    public void addRotateGroupY(Group[][][] cubeletGroup, int y) {
        rotation = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cubeletGroup[i][y][j].getTransforms().add(rotation);
            }
        }
    }
    
    public void addRotateGroupZ(Group[][][] cubeletGroup, int z) {
        rotation = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                cubeletGroup[z][i][j].getTransforms().add(rotation);
            }
        }
    }
    
    public void rotateFcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisZ(0, true);
        addRotateGroupZ(cubeletGroup, 0);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateFcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisZ(0, false);
        addRotateGroupZ(cubeletGroup, 0);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateBcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisZ(2, false);
        addRotateGroupZ(cubeletGroup, 2);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateBcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisZ(2, true);
        addRotateGroupZ(cubeletGroup, 2);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateUcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisY(0, true);
        addRotateGroupY(cubeletGroup, 0);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateUcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisY(0, false);
        addRotateGroupY(cubeletGroup, 0);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateDcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisY(2, false);
        addRotateGroupY(cubeletGroup, 2);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateDcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisY(2, true);
        addRotateGroupY(cubeletGroup, 2);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateLcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisX(0, false);
        addRotateGroupX(cubeletGroup, 0);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateLcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisX(0, true);
        addRotateGroupX(cubeletGroup, 0);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateRcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisX(2, true);
        addRotateGroupX(cubeletGroup, 2);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }   
    
    public void rotateRcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisX(2, false);
        addRotateGroupX(cubeletGroup, 2);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateMcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisX(1, false);
        addRotateGroupX(cubeletGroup, 1);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateMcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisX(1, true);
        addRotateGroupX(cubeletGroup, 1);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateEcl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisY(1, false);
        addRotateGroupY(cubeletGroup, 1);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void rotateEcc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisY(1, true);
        addRotateGroupY(cubeletGroup, 1);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateScl(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisZ(1, true);
        addRotateGroupZ(cubeletGroup, 1);
        addTimeline(cubeGroup, cubeletGroup, 90.0);
    }
    
    public void rotateScc(Group cubeGroup, Group[][][] cubeletGroup) {
        cube.rotateAxisZ(1, false);
        addRotateGroupZ(cubeletGroup, 1);
        addTimeline(cubeGroup, cubeletGroup, -90.0);
    }
    
    public void shuffleCube(Group cubeGroup, Group[][][] cubeletGroup) {
        shuffling = true;
        addRotateGroupZ(cubeletGroup, 0);
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(rotation.angleProperty(), 0.0)),
                new KeyFrame(Duration.millis(speed * 10), new KeyValue(rotation.angleProperty(), 0.0)));
        timeline.setOnFinished(event -> {
            updateCube3D(cubeGroup, cubeletGroup);
            pickRotation(cubeGroup, cubeletGroup, rand.nextInt(18));
        });
        timeline.play();
    }
    
    public void pickRotation(Group cubeGroup, Group[][][] cubeletGroup, int n) {
        switch(n) {
            case(0): rotateFcl(cubeGroup, cubeletGroup); break;
            case(1): rotateFcc(cubeGroup, cubeletGroup); break;
            case(2): rotateBcl(cubeGroup, cubeletGroup); break;
            case(3): rotateBcc(cubeGroup, cubeletGroup); break;
            case(4): rotateUcl(cubeGroup, cubeletGroup); break;
            case(5): rotateUcc(cubeGroup, cubeletGroup); break;
            case(6): rotateDcl(cubeGroup, cubeletGroup); break;
            case(7): rotateDcc(cubeGroup, cubeletGroup); break;
            case(8): rotateLcl(cubeGroup, cubeletGroup); break;
            case(9): rotateLcc(cubeGroup, cubeletGroup); break;
            case(10): rotateRcl(cubeGroup, cubeletGroup); break;
            case(11): rotateRcc(cubeGroup, cubeletGroup); break;
            case(12): rotateMcl(cubeGroup, cubeletGroup); break;
            case(13): rotateMcc(cubeGroup, cubeletGroup); break;
            case(14): rotateEcl(cubeGroup, cubeletGroup); break;
            case(15): rotateEcc(cubeGroup, cubeletGroup); break;
            case(16): rotateScl(cubeGroup, cubeletGroup); break;
            case(17): rotateScc(cubeGroup, cubeletGroup); break;
        }
    }
    
    
    public void solveWhiteCross(Group cubeGroup, Group[][][] cubeletGroup) {
        if(cube.getCubelet(1, 0, 1).getU() != 3) {  //White
            
        }
    }
    
    public void solve(Group cubeGroup, Group[][][] cubeletGroup) {
        solveWhiteCross(cubeGroup, cubeletGroup);
    }
}
