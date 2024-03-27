package org.come.model;

import come.tool.FightingData.Sepcies_MixDeal;
import org.apache.commons.lang.StringUtils;
import org.come.action.role.RoleTransAction;

import java.math.BigDecimal;

public class GolemDraw {
    private String type;
    private String race;
    private BigDecimal id;
    private String value;

    public boolean isMatching(BigDecimal speciesId) {
        if (StringUtils.isBlank(this.race)) return true;
        if (this.race.equals("全部")) return true;
        if (Sepcies_MixDeal.getLocalName(speciesId.intValue()).equals(race)) {// 指定角色
            return true;
        }
        String race = RoleTransAction.getSepciesN(speciesId);
        if (race != null) return race.equals(this.race);
        return false;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getRace() {
        return race;
    }
    public void setRace(String race) {
        this.race = race;
    }
    public BigDecimal getId() {
        return id;
    }
    public void setId(BigDecimal id) {
        this.id = id;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
