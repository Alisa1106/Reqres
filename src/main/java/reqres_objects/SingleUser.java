package reqres_objects;

import com.google.gson.annotations.Expose;
import lombok.Builder;

@lombok.Data
@Builder
public class SingleUser {
    @Expose
    Users data;
    @Expose
    Support support;
}