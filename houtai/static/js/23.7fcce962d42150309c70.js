webpackJsonp([23],{GF4k:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var n={components:{SysName:r("D4f1").a},name:"login",data:function(){return{logining:!1,ruleForm:{username:"",password:""},rules:{username:[{required:!0,message:"请输入用户名",trigger:"blur"}],password:[{required:!0,message:"请输入密码",trigger:"blur"}]}}},methods:{submitForm:function(e){var t=this;this.$refs[e].validate(function(e){if(t.logining=!0,!e)return t.$message.error("登录信息填写格式有误"),!1;var r=new FormData;r.append("UserName",t.ruleForm.username),r.append("Password",t.ruleForm.password),t.$http.post("/api/login",r).then(function(e){200==e.data.code?(t.$message({message:"登录成功，页面即将跳转",type:"success"}),localStorage.setItem("token",e.data.data),localStorage.setItem("username",t.ruleForm.username),t.$router.push("/")):(t.logining=!1,t.ruleForm.password="",t.$alert("用户名或密码错误","提示",{confirmButtonText:"ok"}))}).catch(function(e){t.logining=!1,console.log(e)})})}},mounted:function(){var e=document.getElementById("canvas"),t=e.getContext("2d");e.width=e.parentNode.offsetWidth,e.height=e.parentNode.offsetHeight,console.log(e.width,e.height),window.requestAnimFrame=window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(e){window.setTimeout(e,1e3/60)};var r=40,n=e.height-150,o=0,a=["rgba(69, 159, 117, 0.1)","rgba(95, 170, 135, 0.6)","rgba(69, 159, 117, 0.4)"];!function s(){t.clearRect(0,0,e.width,e.height),o++;for(var i=a.length-1;i>=0;i--){t.fillStyle=a[i];var l=(o+70*i)*Math.PI/180,u=Math.sin(l)*r,m=Math.cos(l)*r;t.beginPath(),t.moveTo(0,n+u),t.moveTo(0,n+u),t.bezierCurveTo(e.width/2,n+u-r,e.width/2,n+m-r,e.width,n+m),t.lineTo(e.width,e.height),t.lineTo(0,e.height),t.lineTo(0,n+u),t.closePath(),t.fill()}requestAnimFrame(s)}()}},o={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"login-wrapper"},[r("div",{staticClass:"login-main"},[r("h3",{staticClass:"login-title"},[r("sys-name")],1),e._v(" "),r("el-form",{ref:"ruleForm",attrs:{model:e.ruleForm,rules:e.rules}},[r("el-form-item",{attrs:{prop:"username"}},[r("el-input",{attrs:{placeholder:"用户名"},model:{value:e.ruleForm.username,callback:function(t){e.$set(e.ruleForm,"username",t)},expression:"ruleForm.username"}})],1),e._v(" "),r("el-form-item",{attrs:{prop:"password"}},[r("el-input",{attrs:{type:"password",placeholder:"密码"},nativeOn:{keyup:function(t){return!t.type.indexOf("key")&&e._k(t.keyCode,"enter",13,t.key,"Enter")?null:e.submitForm("ruleForm")}},model:{value:e.ruleForm.password,callback:function(t){e.$set(e.ruleForm,"password",t)},expression:"ruleForm.password"}})],1),e._v(" "),r("el-form-item",[r("el-button",{staticClass:"login-btn",attrs:{type:"primary",loading:e.logining},on:{click:function(t){return e.submitForm("ruleForm")}}},[e._v("登录")]),e._v(" "),r("p",{staticClass:"login-tip"},[e._v("@275064733")])],1)],1)],1),e._v(" "),r("canvas",{staticClass:"login-anim",attrs:{id:"canvas"}})])},staticRenderFns:[]};var a=r("VU/8")(n,o,!1,function(e){r("KFqd")},"data-v-1a01e424",null);t.default=a.exports},KFqd:function(e,t){}});
//# sourceMappingURL=23.7fcce962d42150309c70.js.map