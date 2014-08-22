﻿/*
Copyright 2013, KISSY UI Library v1.30
MIT Licensed
build time: Jan 17 14:28
*/
/**
 * strike-through command
 * @author yiminghe@gmail.com
 */
KISSY.add("editor/plugin/strike-through/cmd", function (S, Editor, Cmd) {

    var STRIKE_STYLE = new Editor.Style({
        element:'del',
        overrides:[
            {
                element:'span',
                attributes:{
                    style:'text-decoration: line-through;'
                }
            },
            {
                element:'s'
            },
            {
                element:'strike'
            }
        ]
    });
    return {
        init:function (editor) {
            Cmd.addButtonCmd(editor, "strikeThrough", STRIKE_STYLE);
        }
    }
}, {
    requires:['editor', '../font/cmd']
});
