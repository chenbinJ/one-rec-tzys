package com.ztgeo.general.rest.test_entity;


import java.util.List;

public class GSCommonCheckEntity{
    private EntIdx ent_idx;
    private String regstateCn;
    private String entchk_checkres_key;
    private String _map_;
    private List<Diff> diff_list;

    public EntIdx getEnt_idx() {
        return ent_idx;
    }

    public void setEnt_idx(EntIdx ent_idx) {
        this.ent_idx = ent_idx;
    }

    public String getRegstateCn() {
        return regstateCn;
    }

    public void setRegstateCn(String regstateCn) {
        this.regstateCn = regstateCn;
    }

    public String getEntchk_checkres_key() {
        return entchk_checkres_key;
    }

    public void setEntchk_checkres_key(String entchk_checkres_key) {
        this.entchk_checkres_key = entchk_checkres_key;
    }

    public String is_map_() {
        return _map_;
    }

    public void set_map_(String _map_) {
        this._map_ = _map_;
    }

    public List<Diff> getDiff_list() {
        return diff_list;
    }

    public void setDiff_list(List<Diff> diff_list) {
        this.diff_list = diff_list;
    }
}
