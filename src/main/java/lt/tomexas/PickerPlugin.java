package lt.tomexas;

import com.google.gson.Gson;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import lt.tomexas.Panels.MainPluginPanel;
import lt.tomexas.Scheduler.SchedulerTask;
import lt.tomexas.Utils.QuestInfo;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Quest;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

@Slf4j
@PluginDescriptor(
        name = "Random Quest Picker",
        description = "Generates a random quest for you to do",
        tags = {"quest", "guide", "optimal"},
        loadWhenOutdated = true
)
public class PickerPlugin extends Plugin {

    @Inject
    private Client c;

    @Inject
    private PickerConfig config;

    @Inject
    private ClientThread cThread;

    @Inject
    private ClientToolbar cToolbar;

    private static PickerPlugin instance;

    private NavigationButton nBtn;
    private MainPluginPanel mainPluginPanel;

    private final SchedulerTask task = new SchedulerTask();

    private final HashMap<String, QuestInfo> infoMap = new HashMap<>();

    @Override
    protected void startUp() {
        // Parse the quests.json to be loaded into the panel.
        InputStream questDataFile = PickerPlugin.class.getResourceAsStream("/quests.json");
        QuestInfo[] infos = new Gson().fromJson(new InputStreamReader(questDataFile), QuestInfo[].class);

        // Populate HashMap for lookup
        for (int i = 0; i < infos.length; i++) {
            QuestInfo info = infos[i];
            if (info.isMiniquest()) continue;

            info.setIndex(i);
            infoMap.put(info.getName(), info);
        }

        instance = this;
        mainPluginPanel = new MainPluginPanel(c, config, infos);

        // Setup the icon.
        final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/panel_icon.png");

        // Build the navigation button that shows on the sidebar.
        nBtn = NavigationButton.builder()
                .tooltip("Random Quest Picker")
                .icon(icon)
                .priority(7)
                .panel(mainPluginPanel)
                .build();

        // Add the button to the sidebar.
        cToolbar.addNavigation(nBtn);

        task.runUpdateUI(() -> {
            if (c.getGameState().equals(GameState.LOGGED_IN)) cThread.invokeLater(this::updateQuestReqs);
            else mainPluginPanel.hide();
        });

        // load selected quest
        /*if (this.config.selectedData() != null)
            for(QuestInfo info : infos)
                if (info.getName().equalsIgnoreCase(this.config.selectedData()))
                    gPanel.setSelectedQuest(info);*/
    }

    @Override
    protected void shutDown() {
        cToolbar.removeNavigation(nBtn);
    }

    @Provides
    PickerConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(PickerConfig.class);
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged e) {
        // Check the group firing the event & if we are logged in.
        if (!e.getGroup().equalsIgnoreCase("optimal-quest-guide")) return;
        if (!c.getGameState().equals(GameState.LOGGED_IN)) return;
    }

    private void updateQuestReqs() {
        for (Quest quest : Quest.values()) {
            if (isMiniquest(quest.getName())) continue;

            QuestInfo info = infoMap.get(quest.getName());
            if (info == null) {
                //System.out.print("Unknown quest: " + quest.getName() + "\n");
                continue;
            }

            info.setWidget(quest);
            info.setQuestState(quest.getState(c));
        }

        mainPluginPanel.updatePanels(getInfoArray());
    }

    private boolean isMiniquest(String quest) {
        // Needs a better way to check if it's a miniquest
        switch (quest) {
            case "Alfred Grimhand's Barcrawl":
            case "Architectural Alliance":
            case "Bear Your Soul":
            case "Curse of the Empty Lord":
            case "Daddy's Home":
            case "The Enchanted Key":
            case "Enter the Abyss":
            case "Family Pest":
            case "The Frozen Door":
            case "The General's Shadow":
            case "Hopespear's Will":
            case "In Search of Knowledge":
            case "Lair of Tarn Razorlor":
            case "Mage Arena I":
            case "Mage Arena II":
            case "Skippy and the Mogres":
                return true;
        }
        return false;
    }

    /**
     * Returns the local infoMap as a/an QuestInfo[]
     * @return QuestInfo[] array of QuestInfo
     */
    private QuestInfo[] getInfoArray() {
        QuestInfo[] infos = infoMap.values().toArray(new QuestInfo[0]);
        Arrays.sort(infos, Comparator.comparingInt(QuestInfo::getIndex));

        return infos;
    }

    public MainPluginPanel getMainPluginPanel() {
        return mainPluginPanel;
    }

    public static PickerPlugin getInstance() {
        return instance;
    }
}
