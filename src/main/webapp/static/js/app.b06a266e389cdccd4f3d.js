webpackJsonp([30], {
    "5byc": function (e, t) {
    }, AXE3: function (e, t) {
    }, CuRo: function (e, t) {
    }, D4f1: function (e, t, n) {
        "use strict";
        var o = {
            render: function () {
                var e = this.$createElement;
                return (this._self._c || e)("span", [this._v("管理系统")])
            }, staticRenderFns: []
        }, a = n("VU/8")({}, o, !1, null, null, null);
        t.a = a.exports
    }, KhYh: function (e, t) {
    }, NHnr: function (e, t, n) {
        "use strict";
        Object.defineProperty(t, "__esModule", {value: !0});
        var o = n("//Fk"), a = n.n(o), u = n("7+uW"), l = n("365x"), r = {
            name: "App", created: function () {
                if (sessionStorage.getItem("manage_token")) {
                    console.log(sessionStorage.getItem("manage_token"));
                    Object(l.a)(sessionStorage.getItem("manage_token"));
                    this.$store.dispatch("setIsLogin", !0)
                }
            }
        }, i = {
            render: function () {
                var e = this.$createElement, t = this._self._c || e;
                return t("div", {attrs: {id: "app"}}, [t("router-view")], 1)
            }, staticRenderFns: []
        };
        var s = n("VU/8")(r, i, !1, function (e) {
            n("5byc")
        }, null, null).exports, c = n("/ocq");
        u.default.use(c.a);
        var p = new c.a({
                routes: [{path: "/", redirect: "/index"}, {
                    path: "/", component: function () {
                        return n.e(1).then(n.bind(null, "PRim"))
                    }, meta: {title: "公共部分"}, children: [{
                        path: "/index", component: function () {
                            return n.e(5).then(n.bind(null, "15F1"))
                        }, meta: {title: "运行总览"}
                    }, {
                        path: "/send-msg", component: function () {
                            return n.e(24).then(n.bind(null, "bZJn"))
                        }, meta: {title: "游戏公告管理"}
                    }, {
                        path: "/rechargeinfo", component: function () {
                            return n.e(22).then(n.bind(null, "4PjJ"))
                        }, meta: {title: "充值记录查询"}
                    }, {
                        path: "/spread-code", component: function () {
                            return n.e(20).then(n.bind(null, "uXRB"))
                        }, meta: {title: "推广礼包管理"}
                    }, {
                        path: "/player-info", component: function () {
                            return n.e(26).then(n.bind(null, "O1Xo"))
                        }, meta: {title: "玩家信息管理"}
                    }, {
                        path: "/lhconfig", component: function () {
                            return n.e(10).then(n.bind(null, "YYLW"))
                        }, meta: {title: "炼化配置管理"}
                    }, {
                        path: "/yxnrgl/payVipList", component: function () {
                            return Promise.all([n.e(0), n.e(21)]).then(n.bind(null, "Sgp7"))
                        }, meta: {title: "VIP礼包维护"}
                    }, {
                        path: "/yxnrgl/chongjipackList", component: function () {
                            return Promise.all([n.e(0), n.e(15)]).then(n.bind(null, "DKMx"))
                        }, meta: {title: "活动礼包维护"}
                    }, {
                        path: "/yygl/gift", component: function () {
                            return Promise.all([n.e(0), n.e(13)]).then(n.bind(null, "FgY6"))
                        }, meta: {title: "礼包管理"}
                    }, {
                        path: "/yxnrgl/SaveGameData", component: function () {
                            return Promise.all([n.e(0), n.e(18)]).then(n.bind(null, "cbhK"))
                        }, meta: {title: "数据保存"}
                    }, {
                        path: "/yygl/laborClear", component: function () {
                            return Promise.all([n.e(0), n.e(16)]).then(n.bind(null, "IwvZ"))
                        }, meta: {title: "抽奖清理"}
                    }, {
                        path: "/hqgl/recoveryMerge", component: function () {
                            return Promise.all([n.e(0), n.e(14)]).then(n.bind(null, "fpZJ"))
                        }, meta: {title: "恢复合区"}
                    }, {
                        path: "/hqgl/dateBackUp", component: function () {
                            return Promise.all([n.e(0), n.e(8)]).then(n.bind(null, "ZPDW"))
                        }, meta: {title: "数据备份"}
                    }, {
                        path: "/hqgl/ModifyInviteCode", component: function () {
                            return Promise.all([n.e(0), n.e(3)]).then(n.bind(null, "r9dQ"))
                        }, meta: {title: "修改推荐码"}
                    }, {
                        path: "/yhxxgl/zhmmgl", component: function () {
                            return Promise.all([n.e(0), n.e(27)]).then(n.bind(null, "N+b2"))
                        }, meta: {title: "账号密码管理"}
                    }, {
                        path: "/yygl/goods", component: function () {
                            return Promise.all([n.e(0), n.e(17)]).then(n.bind(null, "Qnig"))
                        }, meta: {title: "发送物资"}
                    }, {
                        path: "/rechargeList", component: function () {
                            return Promise.all([n.e(0), n.e(7)]).then(n.bind(null, "uc8g"))
                        }, meta: {title: "仙玉充值"}
                    }, {
                        path: "/rolePassWordList", component: function () {
                            return Promise.all([n.e(0), n.e(4)]).then(n.bind(null, "W7Pv"))
                        }, meta: {title: "修改解锁码"}
                    }, {
                        path: "/upconfig", component: function () {
                            return n.e(9).then(n.bind(null, "lySn"))
                        }, meta: {title: "常规配置管理"}
                    }, {
                        path: "/account-manager", component: function () {
                            return n.e(11).then(n.bind(null, "fzf3"))
                        }, meta: {title: "账号封停管理"}
                    }, {
                        path: "/chart-complex", component: function () {
                            return n.e(2).then(n.bind(null, "0JQf"))
                        }, meta: {title: "游戏登录统计"}
                    }, {
                        path: "/tab", component: function () {
                            return n.e(6).then(n.bind(null, "Oodt"))
                        }, meta: {title: "tab选项卡"}
                    }, {
                        path: "/table", component: function () {
                            return n.e(25).then(n.bind(null, "+HY0"))
                        }, meta: {title: "表格"}
                    }]
                }, {
                    path: "/login", component: function () {
                        return n.e(23).then(n.bind(null, "GF4k"))
                    }
                }, {
                    path: "/register", component: function () {
                        return n.e(19).then(n.bind(null, "0OPg"))
                    }
                }, {
                    path: "/error", component: function () {
                        return n.e(12).then(n.bind(null, "rIjk"))
                    }
                }, {
                    path: "/404", component: function () {
                        return n.e(28).then(n.bind(null, "3bH0"))
                    }
                }]
            }), f = n("zL8q"), d = n.n(f), m = (n("KhYh"), n("AXE3"), n("mPoC"), n("tvR6"), n("mtWM")), h = n.n(m),
            g = n("XLwt"), b = n.n(g), v = n("NYxO"), x = n("mvHQ"), y = n.n(x), I = {
                state: {
                    isCollapse: !1,
                    logoShow: !1,
                    rightNav: {},
                    tabnavBox: JSON.parse(sessionStorage.getItem("addTab")) || [{title: "主页", path: "/index"}]
                }, mutations: {
                    addTab: function (e, t) {
                        e.isActive = t.path, e.tabnavBox[0] && "主页" !== e.tabnavBox[0].title && e.tabnavBox.unshift({
                            title: "主页",
                            path: "/index"
                        });
                        for (var n = 0; n < e.tabnavBox.length; n++) if (e.tabnavBox[n].path === t.path) return !1;
                        e.tabnavBox.push({title: t.title, path: t.path}), sessionStorage.setItem("addTab", y()(e.tabnavBox))
                    }, openMenu: function (e, t) {
                        e.rightNav = t
                    }, removeTab: function (e, t) {
                        var n = e.tabnavBox.findIndex(function (e, n) {
                            return e.path === t.tabItem.path
                        });
                        if (e.tabnavBox.splice(n, 1), t.tabItem.path === t.fullPath) {
                            var o = e.tabnavBox[n] || e.tabnavBox[n - 1];
                            t.router.push(o.path)
                        }
                        sessionStorage.setItem("addTab", y()(e.tabnavBox))
                    }, removeOtherTab: function (e, t) {
                        if (e.tabnavBox = [{title: "主页", path: "/index"}], t.all) return t.router.push("/index"), !1;
                        e.tabnavBox.push(t.tabItem), sessionStorage.setItem("addTab", y()(e.tabnavBox))
                    }, collapse: function (e, t) {
                        e.isCollapse = !e.isCollapse, e.logoShow ? setTimeout(function () {
                            e.logoShow = !1
                        }, 300) : e.logoShow = !0
                    }
                }, actions: {
                    addTab: function (e, t) {
                        (0, e.commit)("addTab", t)
                    }, openMenu: function (e, t) {
                        (0, e.commit)("openMenu", t)
                    }, removeTab: function (e, t) {
                        (0, e.commit)("removeTab", t)
                    }, removeOtherTab: function (e, t) {
                        (0, e.commit)("removeOtherTab", t)
                    }, collapse: function (e, t) {
                        (0, e.commit)("collapse", t)
                    }
                }
            };
        u.default.use(v.a);
        var B = new v.a.Store({
            state: {isLogin: !1, user: {}}, mutations: {
                setIsLogin: function (e, t) {
                    e.isLogin = t
                }, setUser: function (e, t) {
                    t ? e.user = t : t = {}
                }
            }, getters: {
                getIsLogin: function (e) {
                    return e.isLogin
                }, getUser: function (e) {
                    return e.user
                }, tabnavBox: function (e) {
                    return e.layout.tabnavBox
                }, getRightMenu: function (e) {
                    return e.layout.rightMenu
                }, isCollapse: function (e) {
                    return e.layout.isCollapse
                }, visible: function (e) {
                    return e.layout.visible
                }, left: function (e) {
                    return e.layout.left
                }, top: function (e) {
                    return e.layout.top
                }, logoShow: function (e) {
                    return e.layout.logoShow
                }, rightNav: function (e) {
                    return e.layout.rightNav
                }
            }, actions: {
                setIsLogin: function (e, t) {
                    (0, e.commit)("setIsLogin", t)
                }, setUser: function (e, t) {
                    (0, e.commit)("setUser", t)
                }
            }, modules: {layout: I}
        });
        n("CuRo");
        u.default.use(f.Button), u.default.use(f.Carousel), u.default.use(f.CarouselItem), u.default.use(f.Form), u.default.use(f.FormItem), u.default.use(f.Input), u.default.use(f.Card), u.default.use(f.Dropdown), u.default.use(f.DropdownMenu), u.default.use(f.DropdownItem), u.default.use(f.Badge), u.default.use(f.Collapse), u.default.use(f.CollapseItem), u.default.use(f.Table), u.default.use(f.TableColumn), u.default.use(f.Loading.directive), u.default.use(f.Pagination), u.default.use(f.Container), u.default.use(f.Menu), u.default.use(f.Submenu), u.default.use(f.MenuItem), u.default.use(f.MenuItemGroup), u.default.use(f.Aside), u.default.use(f.Main), u.default.use(f.Footer), u.default.use(f.Header), u.default.use(f.Tooltip), u.default.use(f.Row), u.default.use(f.Col), u.default.use(f.Tabs), u.default.use(f.TabPane), u.default.use(f.Dialog), u.default.use(f.Select), u.default.use(f.Option), u.default.use(f.OptionGroup), u.default.use(f.Upload), u.default.use(f.Tag), u.default.prototype.$loading = f.Loading.service, u.default.prototype.$msgbox = f.MessageBox, u.default.prototype.$alert = f.MessageBox.alert, u.default.prototype.$confirm = f.MessageBox.confirm, u.default.prototype.$prompt = f.MessageBox.prompt, u.default.prototype.$notify = Notification, u.default.prototype.$message = f.Message;
        var S = n("D4f1");
        h.a.interceptors.request.use(function (e) {
            return localStorage.getItem("token") && (e.headers.token = localStorage.getItem("token")), e
        }, function (e) {
            return a.a.reject(e)
        }), h.a.interceptors.response.use(function (e) {
            return 401 == e.data.code && (localStorage.clear(), p.replace({path: "/login"})), e
        }, function (e) {
            if (401 !== e.response.status) return a.a.reject(e);
            localStorage.clear(), p.replace({path: "/login"})
        }), u.default.component("sys-name", S.a), h.a.defaults.baseURL = "/GameServer/", u.default.config.productionTip = !1, u.default.use(d.a, {size: "medium"}), u.default.prototype.$http = h.a, u.default.prototype.$echarts = b.a, p.beforeEach(function (e, t, n) {
            if (0 === e.matched.length) return n("/error"), !1;
            localStorage.getItem("token") || "/login" === e.path || "/register" === e.path ? navigator.userAgent.indexOf("MSIE") > -1 && "/editor" === e.path ? u.default.prototype.$alert("vue-quill-editor组件不兼容IE10及以下浏览器，请使用更高版本的浏览器查看", "浏览器不兼容通知", {confirmButtonText: "确定"}) : n() : n("/login")
        }), new u.default({
            el: "#app", router: p, store: B, render: function (e) {
                return e(s)
            }, components: {App: s}, template: "<App/>"
        })
    }, mPoC: function (e, t) {
    }, tvR6: function (e, t) {
    }
}, ["NHnr"]);
//# sourceMappingURL=app.b06a266e389cdccd4f3d.js.map