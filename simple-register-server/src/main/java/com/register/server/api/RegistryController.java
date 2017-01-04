package com.register.server.api;

import com.register.server.container.Registry;
import com.register.server.param.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Created by kaiwang on 2017/1/4.
 */
@RestController
public class RegistryController {
    @Autowired
    private Registry registry;

    @RequestMapping(value = "/registry/{value}", method = RequestMethod.GET)
    public Set<String> get(@PathVariable("value") String value) {
        return registry.getKeySet(value);
    }

    @RequestMapping(value = "/registry", method = RequestMethod.POST)
    public String post(@RequestBody Param param) {
        if (this.registry.get(param.getKey()) == null) {
            registry.put(param.getKey(), param.getValue());
            return param.getValue();
        }
        return null;
    }

    @RequestMapping(value = "/registry", method = RequestMethod.PUT)
    public String put(@RequestBody Param param) {
        String oldValue = this.registry.get(param.getKey());
        registry.put(param.getKey(), param.getValue());
        return oldValue;
    }

    @RequestMapping(value = "/registry/{key}", method = RequestMethod.DELETE)
    public String delete(@PathVariable String key) {
        String oldValue = this.registry.get(key);
        registry.delete(key);
        return oldValue;
    }
}
