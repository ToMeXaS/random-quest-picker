package lt.tomexas.Panels;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lt.tomexas.PickerConfig;
import lt.tomexas.PickerPlugin;
import lt.tomexas.Utils.QuestInfo;
import net.runelite.api.Client;
import net.runelite.api.QuestState;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.LinkBrowser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
public class QuestPanel extends JPanel {

    private final Client c;
    private final PickerConfig config;
    private final PickerPlugin plugin = PickerPlugin.getInstance();

    private final JPanel qHeader = new JPanel();
    private final JLabel qLabel = new JLabel();

    @Getter
    private final QuestInfo quest;

    public QuestPanel(Client c, PickerConfig config, QuestInfo quest) {
        this.c = c;
        this.config = config;
        this.quest = quest;

        setLayout(new GridBagLayout());
        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        gbc.gridx = 0;

        qHeader.setBorder(new EmptyBorder(5, 0, 5, 0));
        qHeader.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        qLabel.setText(quest.getName());
        qLabel.setFont(FontManager.getRunescapeBoldFont());
        qLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qLabel.setVerticalAlignment(SwingConstants.CENTER);

        addMouseListener(new MouseAdapter() {
            private Color background;

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != 1) return;
                if (plugin.getMainPluginPanel().getSelectedQuest() == null) return;

                background = getBackground();
                setBackground(Color.DARK_GRAY);
                qHeader.setBackground(Color.DARK_GRAY);
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != 1) return;
                if (plugin.getMainPluginPanel().getSelectedQuest() == null) return;

                setBackground(background);
                qHeader.setBackground(background);

                plugin.getMainPluginPanel().getqReqMap().get(plugin.getMainPluginPanel().getSelectedQuest().getName())
                        .setVisible(!plugin.getMainPluginPanel().getqReqMap().get(plugin.getMainPluginPanel().getSelectedQuest().getName()).isVisible()); // Toggle quest panel visibility
                plugin.getMainPluginPanel().getSkillReqMap().get(plugin.getMainPluginPanel().getSelectedQuest().getName())
                        .setVisible(!plugin.getMainPluginPanel().getSkillReqMap().get(plugin.getMainPluginPanel().getSelectedQuest().getName()).isVisible()); // Toggle skill panel visibility
                plugin.getMainPluginPanel().getRewardsMap().get(plugin.getMainPluginPanel().getSelectedQuest().getName())
                        .setVisible(!plugin.getMainPluginPanel().getRewardsMap().get(plugin.getMainPluginPanel().getSelectedQuest().getName()).isVisible()); // Toggle skill panel visibility
            }
        });

        qHeader.add(qLabel, BorderLayout.NORTH);

        // Menu Item(s)
        JMenuItem openWiki = new JMenuItem("Open Wiki Guide");
        openWiki.addActionListener(e -> LinkBrowser.browse(quest.getUrl()));

        // Popup Menu
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
        popupMenu.add(openWiki);

        add(qHeader, gbc);
        gbc.gridy++;

        setComponentPopupMenu(popupMenu);
    }

    public void update(QuestInfo quest) {
        SwingUtilities.invokeLater(() -> {
            if (quest == null) return;
            if (quest.getQuestState() == null) return;

            if (quest.getQuestState() == QuestState.IN_PROGRESS)
                qLabel.setForeground(config.getInProgressColor());
            else if (quest.getQuestState() == QuestState.FINISHED)
                qLabel.setForeground(config.getCompletedColor());
            else
                qLabel.setForeground(config.getNotStartedColor());
        });
    }
}
