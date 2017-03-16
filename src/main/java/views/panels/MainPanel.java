package views.panels;

import de.craften.ui.swingmaterial.MaterialIconButton;
import hello.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import views.MainFrame;
import views.constants.Colors;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private MaterialIconButton button = new MaterialIconButton();
    private boolean serverRunning = false;
    private MaterialIconButton startServerBtn;
    private ConfigurableApplicationContext context;
    private boolean isStarting = false;

    public MainPanel(){
        this.setLayout(new BorderLayout());
        init();
        initActionButtons();
    }

    private void initActionButtons() {
        MaterialIconButton addBtn = new MaterialIconButton();
        addBtn.setIcon(new ImageIcon(getClass().getResource("/add.png")));
        MainFrame.getInstance().getToolBar().addActionButton(addBtn);

        startServerBtn = new MaterialIconButton();
        setServerButtonIcon();
        startServerBtn.addActionListener(e -> {
            if (!isStarting) {
                if (!serverRunning) {
                    new Thread(() -> {
                        isStarting = true;
                        context = SpringApplication.run(Application.class);
                        serverRunning = true;
                        isStarting = false;
                        setServerButtonIcon();
                    }).start();
                } else {
                    if (context != null) {
                        SpringApplication.exit(context);
                        serverRunning = false;
                    }
                }
            }
            setServerButtonIcon();
        });
        MainFrame.getInstance().getToolBar().addActionButton(startServerBtn);
    }

    public void setServerButtonIcon(){
        if (!serverRunning) {
            startServerBtn.setIcon(new ImageIcon(getClass().getResource("/play.png")));
        } else {
            startServerBtn.setIcon(new ImageIcon(getClass().getResource("/stop.png")));
        }
    }

    private void init() {
        //button.setType(MaterialButton.Type.RAISED);
        button.setEnabled(true);
        button.setBackground(Colors.ACCENT);
        //button.setMinimumSize(new Dimension(300, 100));
        button.setPreferredSize(new Dimension(56, 56));
        button.setIcon(new ImageIcon(getClass().getResource("/add.png")));
    }

}
