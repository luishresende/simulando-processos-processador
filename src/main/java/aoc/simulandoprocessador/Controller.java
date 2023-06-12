package aoc.simulandoprocessador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import javafx.animation.KeyValue;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import javafx.animation.AnimationTimer;

public class Controller {
    //Definindo elementos do arquivo FXML para a interrupção simples
    @FXML
    public Tab interrupcao_simples;
    @FXML
    private ProgressBar interrupcao_simples_pb1;
    @FXML
    private ProgressBar interrupcao_simples_pb2;
    @FXML
    private ProgressIndicator interrupcao_simples_pi1;
    @FXML
    private ProgressIndicator interrupcao_simples_pi2;
    @FXML
    private TextField interrupcao_simples_processo1;
    @FXML
    private TextField interrupcao_simples_processo2;
    @FXML
    private TextField interrupcao_simples_seg1;
    @FXML
    private TextField interrupcao_simples_seg2;
    @FXML
    private TextArea interrupcao_simples_relatorio;
    //Timelines da interrupção simples
    private Timeline timelineIS1 = new Timeline();
    private Timeline timelineIS2 = new Timeline();

    //Definindo elementos do arquivo FXML para a interrupção multipla
    @FXML
    public Tab interrupcao_multipla;
    @FXML
    private ProgressBar interrupcao_multipla_pb1;
    @FXML
    private ProgressBar interrupcao_multipla_pb2;
    @FXML
    private ProgressBar interrupcao_multipla_pb3;
    @FXML
    private ProgressIndicator interrupcao_multipla_pi1;
    @FXML
    private ProgressIndicator interrupcao_multipla_pi2;
    @FXML
    private ProgressIndicator interrupcao_multipla_pi3;
    @FXML
    private TextField interrupcao_multipla_processo1;
    @FXML
    private TextField interrupcao_multipla_processo2;
    @FXML
    private TextField interrupcao_multipla_processo3;
    @FXML
    private TextField interrupcao_multipla_seg1;
    @FXML
    private TextField interrupcao_multipla_seg2;
    @FXML
    private TextField interrupcao_multipla_seg3;
    @FXML
    private TextArea interrupcao_multipla_relatorio;

    //Timelines da interrupção múltipla
    private Timeline timelineIM1 = new Timeline();
    private Timeline timelineIM2 = new Timeline();
    private Timeline timelineIM3 = new Timeline();

    private Random random = new Random();
    private boolean isSecondBarActive = false;
    private boolean isThirdBarActive = false;
    private DecimalFormat df = new DecimalFormat("#.##");
    private final String[] processos1 = {"Gravando DVD", "Renderizando Vídeo", "Formatando pendrive"};
    private final String[] processos2 = {"Lendo arquivo", "Excluindo arquivo", "Criando arquivo"};
    private final String[] processos3 = {"Decodificando arq B64", "Varredura no HD", "Busca no database"};

    private String[] processos = new String[50]; // Array que receberá os processos simulados
    private int[] duracaoProcessos = new int[50]; // Array que receberá a duração dos processos simulados
    private int numProcessos = 0; // Váriavel que irá armazenar o número de processos percorridos

    // Definindo a animação/lógica da interrupção simples
    private  AnimationTimer AnimationIS = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if(isSecondBarActive){
                interrupcao_simples_seg2.setText(df.format(timelineIS2.getTotalDuration().subtract(timelineIS2.getCurrentTime()).toSeconds()) + "s");
                if(interrupcao_simples_pb2.getProgress() == 1.0){
                    FinishIS2();
                }
            } else{
                interrupcao_simples_seg1.setText(df.format(timelineIS1.getTotalDuration().subtract(timelineIS1.getCurrentTime()).toSeconds()) + "s");
                checkNewProcessIS();
            }
            if(interrupcao_simples_pb1.getProgress() == 1){
                AnimationIS.stop();
                showProcesses();

            }
        }

    };

    // Definindo a animação/lógica da interrupção múltipla
    private AnimationTimer AnimationIM = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if(isThirdBarActive){ // Se a terceira barra estiver ativa, atualizo o tempo dela e verifico se ela já está finalizada
                interrupcao_multipla_seg3.setText(df.format(timelineIM3.getTotalDuration().subtract(timelineIM3.getCurrentTime()).toSeconds()) + "s");
                if(interrupcao_multipla_pb3.getProgress() == 1.0){
                    FinishIM3();
                }
            }else if(isSecondBarActive){ // Se a segunda barra estiver ativa, atualizo o tempo dela, verifico se ela já está finalizada, caso não, verifico se há um novo processo.
                interrupcao_multipla_seg2.setText(df.format(timelineIM2.getTotalDuration().subtract(timelineIM2.getCurrentTime()).toSeconds()) + "s");
                if(interrupcao_multipla_pb2.getProgress() == 1.0){
                    FinishIM2();
                }else{
                    checkNewProcessIM();
                }
            }else{ // Atualizo o timer da barra 1, e checo se há novos processos.
                interrupcao_multipla_seg1.setText(df.format(timelineIM1.getTotalDuration().subtract(timelineIM1.getCurrentTime()).toSeconds()) + "s");
                if(interrupcao_multipla_pb1.getProgress() == 1){ // Verifico se a barra principal chegou ao fim
                    AnimationIM.stop();
                    showProcesses();
                }
                checkNewProcessIM();
            }
        }

    };

    public void initialize() { //Inicialização do controlador, definindo eventos para quando as abas forem trocadas.
        interrupcao_simples.setOnSelectionChanged(event -> {
            if (interrupcao_simples.isSelected()) {
                FinishIM();
                StartIS1();
            }
        });

        interrupcao_multipla.setOnSelectionChanged(event -> {
            if(interrupcao_multipla.isSelected()){
                FinishIS();
                StartIM1();
            }
        });
    }

    private void StartIS1(){ // Inicio a interrupção simples
        int duracao = random.nextInt(11) + 30; // Duração total em segundos
        //Defino KeyValues para as animações dos indicadores
        KeyValue keyValueISPB1 = new KeyValue(interrupcao_simples_pb1.progressProperty(), 1.0);
        KeyValue keyValueISPI1 = new KeyValue(interrupcao_simples_pi1.progressProperty(), 1.0);

        //Crio uma nova keyFrame para a animação dos indicadores
        KeyFrame keyFrameIS1 = new KeyFrame(Duration.seconds(duracao), keyValueISPB1, keyValueISPI1);

        //Adiciono o processo no array
        duracaoProcessos[numProcessos] = duracao;
        processos[numProcessos] = processos1[random.nextInt(processos1.length)];
        interrupcao_simples_processo1.setText(processos[numProcessos]);
        numProcessos++;

        //Crio a nova timeline, adiciono o frame, e inicio o processo simples.
        timelineIS1 = new Timeline();
        timelineIS1.getKeyFrames().add(keyFrameIS1);
        timelineIS1.play();
        AnimationIS.start();
    }

    public void checkNewProcessIS(){ //Função para checar novos processos na interrupção simples.
        int randomInRange = random.nextInt(600);
        if(randomInRange == 250){
            setNewProcessIS();
        }
    }

    private void setNewProcessIS(){ //Função para setar um novo processo na interrupção simples
        isSecondBarActive = true; // Defino a segunda barra como ativa
        timelineIS1.pause(); // Pauno a barra1/timeline1

        //Crio a timeline2 para a barra 2, seguindo a mesma lógica de criação da primeira
        timelineIS2 = new Timeline();

        int duracao = random.nextInt(10) + 2;
        System.out.println("duracao: "+duracao);
        KeyValue keyValueISPB2 = new KeyValue(interrupcao_simples_pb2.progressProperty(), 1.0);
        KeyValue keyValueISPI2 = new KeyValue(interrupcao_simples_pi2.progressProperty(), 1.0);

        KeyFrame keyFrameIS2 = new KeyFrame(Duration.seconds(duracao), keyValueISPB2, keyValueISPI2);

        duracaoProcessos[numProcessos] = duracao;
        processos[numProcessos] = processos2[random.nextInt(processos2.length)];
        interrupcao_simples_processo2.setText(processos[numProcessos]);
        numProcessos++;

        //Deixo os componentes da segunda barra visível
        interrupcao_simples_pb2.setVisible(true);
        interrupcao_simples_pi2.setVisible(true);
        interrupcao_simples_processo2.setVisible(true);
        interrupcao_simples_seg2.setVisible(true);

        timelineIS2.getKeyFrames().add(keyFrameIS2);
        timelineIS2.play();
    }

    private void FinishIS(){ // Finalizo a interrupção simples e a lista de Arrays
        interrupcao_simples_pb1.setProgress(0);
        interrupcao_simples_pi1.setProgress(0);
        interrupcao_simples_processo1.setText("");

        FinishIS2();
        numProcessos = 0;
        Arrays.fill(processos, null);
        interrupcao_simples_relatorio.clear();
        interrupcao_simples_relatorio.setVisible(false);

        timelineIS1.pause();
        AnimationIS.stop();
        timelineIS1.getKeyFrames().clear();
    }
    private void FinishIS2(){ //Finalizo a barra 2
        timelineIS2.getKeyFrames().clear();
        isSecondBarActive = false;
        interrupcao_simples_pb2.setVisible(false);
        interrupcao_simples_pi2.setVisible(false);
        interrupcao_simples_processo2.setVisible(false);
        interrupcao_simples_seg2.setVisible(false);
        interrupcao_simples_pb2.setProgress(0);
        interrupcao_simples_pi2.setProgress(0);

        if (timelineIS2 != null) {
            timelineIS2.pause();
        }
        timelineIS1.play();
    }

    private void StartIM1(){
        int duracao = random.nextInt(11) + 20; // Duração total em segundos
        KeyValue keyValueIMPB1 = new KeyValue(interrupcao_multipla_pb1.progressProperty(), 1.0);
        KeyValue keyValueIMPB2 = new KeyValue(interrupcao_multipla_pi1.progressProperty(), 1.0);

        KeyFrame keyFrameIM1 = new KeyFrame(Duration.seconds(duracao), keyValueIMPB1, keyValueIMPB2);

        // Adiciono o processo na lista
        duracaoProcessos[numProcessos] = duracao;
        processos[numProcessos] = processos1[random.nextInt(processos1.length)];
        interrupcao_multipla_processo1.setText(processos[numProcessos]);
        numProcessos++;

        timelineIM1 = new Timeline();
        timelineIM1.getKeyFrames().add(keyFrameIM1);
        timelineIM1.play();
        AnimationIM.start();
    }

    public void checkNewProcessIM(){ //Função para checar novos processos na interrupção múltilpa
        Random random = new Random();
        int randomInRange = random.nextInt(500);
        if(randomInRange == 250){
            if(isSecondBarActive && !isThirdBarActive){
                setNewProcessIM2();
            }else{
                setNewProcessIM1();
            }
        }
    }

    private void setNewProcessIM1(){ // Seto novo processo para a interrupção múltipla (barra2)
        isSecondBarActive = true;
        timelineIM1.pause();

        timelineIM2 = new Timeline();

        int duracao = random.nextInt(10) + 2;
        KeyValue keyValueIMPB2 = new KeyValue(interrupcao_multipla_pb2.progressProperty(), 1.0);
        KeyValue keyValueIMPI2 = new KeyValue(interrupcao_multipla_pi2.progressProperty(), 1.0);
        KeyFrame keyFrameIM2 = new KeyFrame(Duration.seconds(duracao), keyValueIMPB2, keyValueIMPI2);

        duracaoProcessos[numProcessos] = duracao;
        processos[numProcessos] = processos2[random.nextInt(processos2.length)];
        interrupcao_multipla_processo2.setText(processos[numProcessos]);
        numProcessos++;

        // Adiciono o processo na lista
        interrupcao_multipla_processo2.setVisible(true);
        interrupcao_multipla_pb2.setVisible(true);
        interrupcao_multipla_pi2.setVisible(true);
        interrupcao_multipla_seg2.setVisible(true);

        timelineIM2.getKeyFrames().add(keyFrameIM2);
        timelineIM2.play();
    }

    private void setNewProcessIM2(){ // Seto novo processo para a interrupção múltipla (barra3)
        isThirdBarActive = true;
        timelineIM2.pause();

        timelineIM3 = new Timeline();

        int duracao = random.nextInt(5) + 2;
        KeyValue keyValueIMPB3 = new KeyValue(interrupcao_multipla_pb3.progressProperty(), 1.0);
        KeyValue keyValueIMPI3 = new KeyValue(interrupcao_multipla_pi3.progressProperty(), 1.0);
        KeyFrame keyFrameIM3 = new KeyFrame(Duration.seconds(duracao), keyValueIMPB3, keyValueIMPI3);

        // Adiciono o processo na lista
        duracaoProcessos[numProcessos] = duracao;
        processos[numProcessos] = processos3[random.nextInt(processos3.length)];
        interrupcao_multipla_processo3.setText(processos[numProcessos]);
        numProcessos++;

        interrupcao_multipla_processo3.setVisible(true);
        interrupcao_multipla_pb3.setVisible(true);
        interrupcao_multipla_pi3.setVisible(true);
        interrupcao_multipla_seg3.setVisible(true);

        timelineIM3.getKeyFrames().add(keyFrameIM3);
        timelineIM3.play();
    }
    private void FinishIM(){
        interrupcao_multipla_pb1.setProgress(0);
        interrupcao_multipla_pi1.setProgress(0);

        FinishIM3();
        FinishIM2();
        numProcessos = 0;
        Arrays.fill(processos, null);
        interrupcao_multipla_relatorio.clear();
        interrupcao_multipla_relatorio.setVisible(false);
        
        timelineIM1.pause();
        AnimationIM.stop();
        timelineIM1.getKeyFrames().clear();
    }
    private void FinishIM2(){
        timelineIM2.getKeyFrames().clear();
        isSecondBarActive = false;
        interrupcao_multipla_pb2.setVisible(false);
        interrupcao_multipla_pi2.setVisible(false);
        interrupcao_multipla_processo2.setVisible(false);
        interrupcao_multipla_seg2.setVisible(false);
        interrupcao_multipla_pb2.setProgress(0);
        interrupcao_multipla_pi2.setProgress(0);

        if (timelineIM2 != null) {
            timelineIM2.pause();
        }
        timelineIM1.play();
    }

    private void FinishIM3(){
        timelineIM3.getKeyFrames().clear();
        isThirdBarActive = false;
        interrupcao_multipla_pb3.setVisible(false);
        interrupcao_multipla_pi3.setVisible(false);
        interrupcao_multipla_processo3.setVisible(false);
        interrupcao_multipla_seg3.setVisible(false);
        interrupcao_multipla_pb3.setProgress(0);
        interrupcao_multipla_pi3.setProgress(0);

        if (timelineIM3 != null) {
            System.out.println("Pausando a timeline 3");
            timelineIM3.pause();
        }
        timelineIM2.play();
    }

    private void showProcesses(){ // Função para gerar a lista de processos.
        StringBuilder stringBuilder = new StringBuilder();
        int duracaoTotal = 0;
        for (int i = 0; i < numProcessos; i++) {
            stringBuilder.append(processos[i]).append(": ").append(duracaoProcessos[i]).append("s\n");
            duracaoTotal += duracaoProcessos[i];

            if(i == numProcessos-1){
                stringBuilder.append("\nDuração total: ").append(duracaoTotal).append("s");
            }
        }
        
        if(interrupcao_multipla.isSelected()){ // Verifico qual aba está selecionada para mostrar corretamente no programa
            interrupcao_multipla_processo2.setVisible(false);
            interrupcao_multipla_relatorio.setText(String.valueOf(stringBuilder));
            interrupcao_multipla_relatorio.setVisible(true);

        } else{
            interrupcao_simples_processo2.setVisible(false);
            interrupcao_simples_relatorio.setText(String.valueOf(stringBuilder));
            interrupcao_simples_relatorio.setVisible(true);
        }
    }
}