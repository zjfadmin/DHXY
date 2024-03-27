
package com.gl.controller;

import com.gl.model.Param;
import com.gl.model.Result;
import com.gl.model.VersionDto;
import com.gl.service.GameService;
import com.gl.service.ResultFactory;
import com.gl.token.UserToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.come.redis.RedisPoolUntil;
import org.come.tool.ReadExelTool;
import org.come.until.ReadTxtUtil;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

@RestController
public class VersionController {

    @UserToken
    @GetMapping({"/version"})
    public Result version(HttpServletRequest request, HttpServletResponse response) {

        String version = request.getParameter("version");

        if (StringUtils.isBlank(version))
            return ResultFactory.fail("未获取到当前游戏版本号！");

        if (!version.contains("v"))
            return ResultFactory.fail("未获取到当前游戏版本号！");

        VersionDto versionDto = new VersionDto();
        String oneString = version.substring(0, 1);
        if (oneString.equals("v")) {
            String currentVersionStr = version.substring(1, version.length());
            try {
                Double currentVersion = Double.parseDouble(currentVersionStr);
                Jedis jedis = RedisPoolUntil.getJedis();
                String gameVersionStr = jedis.get("gameVersion");
                //缓存中没有版本信息 客户端无需更新
                if (StringUtils.isBlank(gameVersionStr)) {
                    versionDto.setCode("success");
                    return ResultFactory.success(versionDto);
                } else {
                    //客户端版本最新无需更新
                    Double gameVersion = Double.parseDouble(gameVersionStr);
                    if (gameVersion.compareTo(currentVersion) == 0) {
                        versionDto.setCode("success");
                    } else {
                        //客户端版本需要更新
                        String historicVersion = jedis.get("historicVersion");
                        String[] historicVersions = historicVersion.split(",");
                        List<String> needAnUpdatedVersion = new ArrayList<>();
                        Boolean b = false;
                        for (int i = 0; i < historicVersions.length; i++) {
                            if (b)
                                needAnUpdatedVersion.add("v" + historicVersions[i]);
                            //找到当前版本的位置
                            if (historicVersions[i].equals(currentVersionStr)) {
                                b = true;
                            }
                        }
                        versionDto.setCode("update");
                        versionDto.setData(needAnUpdatedVersion);
                        //返回需要更新的版本信息
                    }
                    return ResultFactory.success(versionDto, "检测到新的版本");
                }

            } catch (NumberFormatException numberFormatException) {
                return ResultFactory.fail("未获取到当前游戏版本号！");
            }

        } else {
            return ResultFactory.fail("未获取到当前游戏版本号！");
        }
    }


}
