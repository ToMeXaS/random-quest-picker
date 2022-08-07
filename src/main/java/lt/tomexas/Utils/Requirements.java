package lt.tomexas.Utils;

import lombok.Getter;
import lombok.Setter;

public class Requirements {

    @Getter
    @Setter
    private String[] quests;

    @Getter
    @Setter
    private SkillRequirement[] skills;

}
