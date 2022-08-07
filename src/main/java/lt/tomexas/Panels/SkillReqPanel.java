package lt.tomexas.Panels;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.tomexas.PickerConfig;
import lt.tomexas.PickerPlugin;
import lt.tomexas.Utils.QuestInfo;
import lt.tomexas.Utils.SkillRequirement;
import net.runelite.api.*;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class SkillReqPanel extends JPanel {

    private final Client c;
    private final PickerConfig config;

    @Getter
    private final QuestInfo quest;

    private final PickerPlugin plugin = PickerPlugin.getInstance();

    private final JLabel skillReq = new JLabel();

    private final HashMap<String, List<JLabel>> skillMap = new HashMap<>();

    public SkillReqPanel(Client c, PickerConfig config, QuestInfo quest) {
        this.c = c;
        this.config = config;
        this.quest = quest;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        skillReq.setFont(FontManager.getRunescapeBoldFont());
        skillReq.setText("Skill requirements:");
        skillReq.setBorder(new EmptyBorder(0, 0, 5, 0));
        add(skillReq);

        if (quest.getRequirements().getSkills().length > 0)
            addSkillReqs(quest, quest.getRequirements().getSkills());
        else {
            JLabel text = new JLabel();
            text.setText("None");
            add(text);
        }
    }

    private void addSkillReqs(QuestInfo info, SkillRequirement[] skills) {
        List<JLabel> labelList = new ArrayList<>();
        for (SkillRequirement skill : skills) {
            JLabel text = new JLabel();

            text.setText("   " + skill.getLevel() + " " + StringUtils.capitalize(skill.getSkill()));
            text.setFont(FontManager.getRunescapeFont());
            text.setIcon(new ImageIcon(skill.getIcon()));

            labelList.add(text);
        }

        skillMap.put(info.getName(), labelList);
        for (JLabel label : labelList)
            add(label);
    }

    public void update(QuestInfo quest) {
        if (quest == null) return;
        if (quest.getQuestState() == null) return;
        if (quest.getRequirements().getSkills().length == 0) return;

        SkillRequirement[] skillRequirements = quest.getRequirements().getSkills();

        List<JLabel> labelList = skillMap.get(quest.getName());

        for (JLabel label : labelList) {
            for (SkillRequirement requirement : skillRequirements) {
                if (!label.getText().toLowerCase().contains(requirement.getSkill())) continue;

                if (meetsRequirements(requirement.getSkill(), requirement.getLevel()))
                    label.setForeground(config.getRequirementMetColor());
                else if (requirement.isBoostable())
                    label.setText(label.getText() + " (boostable)");
                else {
                    label.setForeground(config.getRequirementUnmetColor());
                }
            }
        }
    }

    private boolean meetsRequirements(String skill, int level) {
        if (skill.equalsIgnoreCase("quest points")) {
            return c.getVarpValue(VarPlayer.QUEST_POINTS.getId()) >= level;
        } else if (skill.equalsIgnoreCase("combat level")) {
            return c.getLocalPlayer().getCombatLevel() >= level;
        } else {
            return c.getRealSkillLevel(Skill.valueOf(skill.toUpperCase())) >= level;
        }
    }
}
