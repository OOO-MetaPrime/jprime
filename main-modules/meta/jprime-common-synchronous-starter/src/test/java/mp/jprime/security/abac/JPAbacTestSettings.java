package mp.jprime.security.abac;

import mp.jprime.common.annotations.JPCond;
import mp.jprime.common.annotations.JPTime;
import mp.jprime.dataaccess.JPAction;
import mp.jprime.dataaccess.enums.FilterValue;
import mp.jprime.security.JPSecuritySettings;
import mp.jprime.security.abac.annotations.*;
import mp.jprime.security.beans.JPAccessType;

import java.time.DayOfWeek;

@JPPolicySets(
    value = {
        @JPPolicySet(
            code = "ef139bbd-ca81-4e09-94e3-48867f6452aa",
            name = "Группа политик annotation-тест",
            jpClasses = {"test1", "test2"},
            policies = {
                @JPPolicy(
                    name = "Политика Создание",
                    actions = {JPAction.CREATE},
                    subjectRules = {
                        @JPSubjectRule(
                            name = "Доступно только роли TEST_ROLE",
                            qName = "jpPolicySet.annotationTest.create.rule1",
                            role = @JPCond(in = {"TEST_ROLE"}),
                            effect = JPAccessType.PERMIT
                        )
                    },
                    environmentRules = {
                        @JPEnvironmentRule(
                            name = "Доступно только в течении для 2010-01-10",
                            time = @JPTime(
                                daysOfWeek = {DayOfWeek.MONDAY, DayOfWeek.THURSDAY},
                                fromTime = "10:00",
                                toTime = "17:59",
                                fromDateTime = "2010-01-10T00:00:00",
                                toDateTime = "2020-01-10T23:59:59"
                            )
                        )
                    }
                ),
                @JPPolicy(
                    name = "Политика Обновление",
                    actions = {JPAction.READ, JPAction.UPDATE},
                    resourceRules = {
                        @JPResourceRule(
                            name = "Доступно только автору",
                            attr = "userOwnerId",
                            cond = @JPCond(in = {FilterValue.AUTH_USERID}),
                            effect = JPAccessType.PERMIT
                        ),
                        @JPResourceRule(
                            name = "Доступно, если организация != null",
                            attr = "org",
                            cond = @JPCond(isNotNull = true),
                            effect = JPAccessType.PERMIT
                        ),
                        @JPResourceRule(
                            name = "Запрещено, если департамент == null",
                            attr = "dep",
                            cond = @JPCond(isNull = true),
                            effect = JPAccessType.PROHIBITION
                        )
                    }
                ),

            }
        )
    }
)
public class JPAbacTestSettings implements JPSecuritySettings {
}
