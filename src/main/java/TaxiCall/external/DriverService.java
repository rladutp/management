
package TaxiCall.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

// 환경파일로 변경 , 추후  Config Map 해줘도 무방 단, application.yml 제거 필요
@FeignClient(name="Driver", url= "${api.url.driver}")
//@FeignClient(name="Driver", url="http://localhost:")
/* 20200910 Cloud 올릴 때 Driver로 바꿔야함 */
//@FeignClient(name="Driver", url="http://Driver:8080")
public interface DriverService {

    @RequestMapping(method= RequestMethod.GET, path="/drivers/check")
    public void checkOrder(@RequestBody Driver param);

}