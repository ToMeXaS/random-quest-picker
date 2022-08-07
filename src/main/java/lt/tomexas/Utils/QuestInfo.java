package lt.tomexas.Utils;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Quest;
import net.runelite.api.QuestState;

public class QuestInfo {
    @Getter
    @Setter
    private int index;

    @Getter
    @Setter
    private boolean miniquest;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String shortName;

    @Getter
    @Setter
    private String url;

    @Getter
    @Setter
    private boolean members;

    @Getter
    @Setter
    private String difficulty;

    @Getter
    @Setter
    private String questLength;

    @Getter
    @Setter
    private Requirements requirements;

    @Getter
    @Setter
    private Rewards rewards;

    @Getter
    @Setter
    private String series;

    @Getter
    @Setter
    private QuestState questState;

    @Getter
    @Setter
    private Quest widget = null;
}

