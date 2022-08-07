package lt.tomexas.Panels;

import lombok.extern.slf4j.Slf4j;
import lt.tomexas.PickerConfig;
import lt.tomexas.Layouts.CollapsingGridLayout;
import lt.tomexas.Scheduler.SchedulerTask;
import lt.tomexas.Utils.QuestInfo;
import net.runelite.api.*;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.util.*;

@Slf4j
public class MainPluginPanel extends PluginPanel {

    private final Client c;
    private final PickerConfig config;

    private final MainPanel mainPanel = new MainPanel();
    private final HashMap<String, QuestPanel> visibleQPanels = new HashMap<>();

    private final SchedulerTask task = new SchedulerTask();

    private final JButton questButton = new JButton();

    private final HashMap<String, QuestPanel> qMap = new HashMap<>();
    private final HashMap<String, QuestReqPanel> qReqMap = new HashMap<>();
    private final HashMap<String, SkillReqPanel> skillReqMap = new HashMap<>();
    private final HashMap<String, RewardsPanel> rewardsMap = new HashMap<>();

    private int visibleCount = 0;
    private QuestInfo selectedQuest = null;

    @Inject
    public MainPluginPanel(Client c, PickerConfig config, QuestInfo[] infos) {
        super();

        this.c = c;
        this.config = config;

        // This number is so fucking dumb, norint pridėt koki papildoma button, gauni padidint by 1..
        //                                                         V
        setLayout(new CollapsingGridLayout(infos.length * 4 + 4, 2, 0, 2));

        mainPanel.setContent("Random Quest Picker", "Please login in order to get a quest!");
        add(mainPanel);

        questButton.setText("Get a quest");
        questButton.setVisible(false);
        questButton.addActionListener(e -> startQuestSelection());
        add(questButton);

        // Add all quests & information hide them and populate the HashMap
        for (QuestInfo info : infos) {
            QuestPanel qPanel = new QuestPanel(c, config, info);
            qPanel.setVisible(false);
            qMap.put(info.getName(), qPanel);
            add(qPanel);

            QuestReqPanel qReqPanel = new QuestReqPanel(c, config, info);
            qReqPanel.setVisible(false);
            qReqMap.put(info.getName(), qReqPanel);
            add(qReqPanel);

            SkillReqPanel skillReqPanel = new SkillReqPanel(c, config, info);
            skillReqPanel.setVisible(false);
            skillReqMap.put(info.getName(), skillReqPanel);
            add(skillReqPanel);

            RewardsPanel rewardsPanel = new RewardsPanel(c, config, info);
            rewardsPanel.setVisible(false);
            rewardsMap.put(info.getName(), rewardsPanel);
            add(rewardsPanel);
        }
    }

    public void updatePanels(QuestInfo[] infos) {
        mainPanel.setContent("Random Quest Picker", "Click the button below to get a quest!");

        questButton.setVisible(true);

        if (selectedQuest == null) {
            // Unhide only the quests that are not finished
            for (QuestInfo info : infos) {
                //System.out.print("Quest state: " + info.getName() + " " + info.getQuestState() + "\n");
                if (info.getQuestState().equals(QuestState.FINISHED)) continue;
                QuestPanel qPanel = qMap.get(info.getName());
                visibleQPanels.put(info.getName(), qPanel);
                qPanel.setVisible(true);
                qPanel.update(info);
            }
        } else {
            for (QuestInfo info : infos) {
                QuestReqPanel qReqPanel = qReqMap.get(info.getName());
                qReqPanel.update(info);

                SkillReqPanel skillReqPanel = skillReqMap.get(info.getName());
                skillReqPanel.update(info);
            }
        }

        revalidate();
    }

    public void hide() {
        mainPanel.setContent("Random Quest Picker", "Please login in order to get a quest!");
        questButton.setVisible(false);

        visibleQPanels.forEach((key, value) -> value.setVisible(false));

        if (selectedQuest != null) {
            qReqMap.get(selectedQuest.getName()).setVisible(false);
            skillReqMap.get(selectedQuest.getName()).setVisible(false);
            rewardsMap.get(selectedQuest.getName()).setVisible(false);

            selectedQuest = null;
        }
    }

    private void startQuestSelection() {
        task.cancelIfNeeded();

        visibleQPanels.forEach((key, value) -> value.setVisible(true));
        visibleCount = visibleQPanels.size();

        if (selectedQuest != null) {
            qReqMap.get(selectedQuest.getName()).setVisible(false);
            skillReqMap.get(selectedQuest.getName()).setVisible(false);
            rewardsMap.get(selectedQuest.getName()).setVisible(false);
        }

        int randomNumber = getRandomNumber(0, visibleQPanels.size());
        selectedQuest = selectQuest(randomNumber);

        task.runQuestSelector(() -> {
            ArrayList<String> keysArray = new ArrayList<>(visibleQPanels.keySet());
            int randomPanel = getRandomNumber(0, visibleQPanels.size());
            if (visibleCount > 1) {
                if (!visibleQPanels.get(keysArray.get(randomPanel)).isVisible()) return; // If panel is already hidden return
                if (visibleQPanels.get(keysArray.get(randomPanel)).getQuest().equals(selectedQuest)) return; // If panel is selectedQuest return
                visibleQPanels.get(keysArray.get(randomPanel)).setVisible(false);
                visibleCount--;
            } else if (visibleCount == 1) {
                //this.config.selectedData(selectedQuest.getName()); //saves selected quest for per session use
                task.cancelSelector();
            }
        });
        revalidate();
    }

    private QuestInfo selectQuest(int index) {
        ArrayList<String> keysArray = new ArrayList<>(visibleQPanels.keySet());
        QuestPanel qp = qMap.get(keysArray.get(index));
        return qp.getQuest();
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public HashMap<String, QuestReqPanel> getqReqMap() {
        return qReqMap;
    }

    public HashMap<String, SkillReqPanel> getSkillReqMap() {
        return skillReqMap;
    }

    public HashMap<String, RewardsPanel> getRewardsMap() {
        return rewardsMap;
    }

    public QuestInfo getSelectedQuest() {
        return selectedQuest;
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }
}
