package loupai.zick.personal.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Zick on 2017/6/2.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface FragmentScope {

}
