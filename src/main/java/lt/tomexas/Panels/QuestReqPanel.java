package lt.tomexas.Panels;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.tomexas.PickerConfig;
import lt.tomexas.PickerPlugin;
import lt.tomexas.Utils.QuestInfo;
import net.runelite.api.Client;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class QuestReqPanel extends JPanel {

    private final Client c;
    private final PickerConfig config;

    @Getter
    private final QuestInfo quest;

    private final PickerPlugin plugin = PickerPlugin.getInstance();

    private final JLabel questReq = new JLabel();

    private final HashMap<String, List<JLabel>> questMap = new HashMap<>();

    public QuestReqPanel(Client c, PickerConfig config, QuestInfo quest) {
        this.c = c;
        this.config = config;
        this.quest = quest;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        questReq.setFont(FontManager.getRunescapeBoldFont());
        questReq.setText("Quest requirements:");
        questReq.setBorder(new EmptyBorder(0, 0, 5, 0));
        add(questReq);

        if (quest.getRequirements().getQuests().length > 0)
            addQuestReqs(quest, quest.getRequirements().getQuests());
        else {
            JLabel text = new JLabel();
            text.setText("None");
            add(text);
        }
    }

    private void addQuestReqs(QuestInfo info, String[] quests) {
        List<JLabel> labelList = new ArrayList<>();
        for (String quest : quests) {
            JLabel text = new JLabel();

            text.setText("   " + quest);
            text.setIcon(new ImageIcon(ImageUtil.loadImageResource(PickerPlugin.class, "/quest_point.png")));

            labelList.add(text);
        }

        questMap.put(info.getName(), labelList);
        for (JLabel label : labelList)
            add(label);
    }

    public void update(QuestInfo quest) {
        if (quest == null) return;
        if (quest.getQuestState() == null) return;
        if (quest.getRequirements().getQuests().length == 0) return;

        List<JLabel> labelList = questMap.get(quest.getName());

        for (JLabel label : labelList) {
            for (Quest checkQuest : Quest.values()) {
                if (label.getText().contains(checkQuest.getName())) {
                    if (checkQuest.getState(c) == QuestState.IN_PROGRESS)
                        label.setForeground(config.getInProgressColor());
                    else if (checkQuest.getState(c) == QuestState.FINISHED)
                        label.setForeground(config.getCompletedColor());
                    else {
                        label.setForeground(config.getNotStartedColor());
                        label.setText(label.getText());
                    }
                }
            }
        }
    }
}
