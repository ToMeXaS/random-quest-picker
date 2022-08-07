package lt.tomexas.Panels;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.tomexas.PickerConfig;
import lt.tomexas.PickerPlugin;
import lt.tomexas.Utils.ExpRewards;
import lt.tomexas.Utils.QuestInfo;
import lt.tomexas.Utils.Rewards;
import lt.tomexas.Utils.SkillRequirement;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.client.game.SkillIconManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.IconTextField;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Slf4j
public class RewardsPanel extends JPanel {

    private final Client c;
    private final PickerConfig config;

    @Getter
    private final QuestInfo quest;

    private final PickerPlugin plugin = PickerPlugin.getInstance();

    private final JLabel rewards = new JLabel();

    private final HashMap<String, List<JLabel>> skillMap = new HashMap<>();

    public RewardsPanel(Client c, PickerConfig config, QuestInfo quest) {
        this.c = c;
        this.config = config;
        this.quest = quest;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(ColorScheme.DARKER_GRAY_COLOR);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        rewards.setFont(FontManager.getRunescapeBoldFont());
        rewards.setText("Rewards:");
        rewards.setBorder(new EmptyBorder(0, 0, 5, 0));
        add(rewards);

        if (quest.getRewards().getExp().length > 0)
            addRewards(quest.getRewards());
        else {
            JLabel text = new JLabel();
            text.setText("None");

            add(text);
        }
    }

    private void addRewards(Rewards rewards) {
        List<JLabel> labelList = new ArrayList<>();
        ExpRewards[] expRewards = rewards.getExp();
        for (ExpRewards exp : expRewards) {
            JLabel text = new JLabel();

            text.setText("   " + NumberFormat.getNumberInstance(Locale.US).format(exp.getAmount()) + " "
                    + StringUtils.capitalize(exp.getSkill()) + " Experience");
            text.setFont(FontManager.getRunescapeFont());
            text.setIcon(new ImageIcon(new SkillIconManager().getSkillImage(Skill.valueOf(exp.getSkill().toUpperCase()), true)));

            labelList.add(text);
        }

        for (JLabel label : labelList)
            add(label);
    }
}
