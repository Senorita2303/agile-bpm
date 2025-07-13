import { _GettersTree } from "pinia";

export interface IglobalState {
    ismobile: boolean;
    // Layout
    layout: string;
    // Is the menu folded toggle
    menuIsCollapse: boolean;
    // Multiple tab bar
    layoutTags: boolean;
    // Theme
    theme: string;
    // Language
    lang: string;
    dark: boolean;
    color: string
}
export interface IglobalGetter extends _GettersTree<IglobalState> {
    GetLang(state: IglobalState): string;
    GetDark(state: IglobalState): boolean;
    GetTags(state: IglobalState): boolean;
    GetColl(state: IglobalState): boolean
    GetLayOut(state: IglobalState): string;
    GetColor(state: IglobalState): string;
}
export interface defaultActions {
    SET_ismobile(key: boolean): void;
    setColor(color: string): void;
    setLayOut(layout: string): void;
    setMenuCollapse(coll: boolean): void;
    setEnableTags(tag: boolean): void;
    SETLang(lang: string): void;
    SETDark(dark: boolean): void;
    clearDark(): void;
    clearLang(): void;
    clearAll(): void;
}