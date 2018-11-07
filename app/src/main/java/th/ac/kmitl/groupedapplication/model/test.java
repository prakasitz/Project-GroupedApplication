package th.ac.kmitl.groupedapplication.model;

import java.util.List;

public class test {

    /**
     * classroom : {"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2":{"class_1":{"class_id":"class_1","class_subject":"โครงสร้างข้อมูลและอัลกอลริทึม","class_day":"พุธ","class_start":"09.30","prof_id":"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2","worked_list":[{"worked_id":"c2w1"}]},"class_3":{"class_id":"class_3","class_subject":"การคิดเชิงตัวเลข","class_day":"พุธ","class_start":"13.30","prof_id":"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2"}},"o7hB4naxmSQV5BVfUt1yX3nb3823":{"class_2":{"class_id":"class_2","class_subject":"อุทกศาสตร์","class_day":"อังคาร","class_start":"09.30","prof_id":"o7hB4naxmSQV5BVfUt1yX3nb3823","worked":{"worked_id":"c1w1"}},"class_4":{"class_id":"class_4","class_subject":"แคลคูลัส 1 สำหรับวิศวะ","class_day":"จันทร์","class_start":"13.30","prof_id":"o7hB4naxmSQV5BVfUt1yX3nb3823"}}}
     */

    private ClassroomBean classroom;

    public ClassroomBean getClassroom() {
        return classroom;
    }

    public void setClassroom(ClassroomBean classroom) {
        this.classroom = classroom;
    }

    public static class ClassroomBean {
        /**
         * y1Pw8FLvkWQQLMhmPcXtQ9tvRup2 : {"class_1":{"class_id":"class_1","class_subject":"โครงสร้างข้อมูลและอัลกอลริทึม","class_day":"พุธ","class_start":"09.30","prof_id":"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2","worked_list":[{"worked_id":"c2w1"}]},"class_3":{"class_id":"class_3","class_subject":"การคิดเชิงตัวเลข","class_day":"พุธ","class_start":"13.30","prof_id":"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2"}}
         * o7hB4naxmSQV5BVfUt1yX3nb3823 : {"class_2":{"class_id":"class_2","class_subject":"อุทกศาสตร์","class_day":"อังคาร","class_start":"09.30","prof_id":"o7hB4naxmSQV5BVfUt1yX3nb3823","worked":{"worked_id":"c1w1"}},"class_4":{"class_id":"class_4","class_subject":"แคลคูลัส 1 สำหรับวิศวะ","class_day":"จันทร์","class_start":"13.30","prof_id":"o7hB4naxmSQV5BVfUt1yX3nb3823"}}
         */

        private Y1Pw8FLvkWQQLMhmPcXtQ9tvRup2Bean y1Pw8FLvkWQQLMhmPcXtQ9tvRup2;
        private O7hB4naxmSQV5BVfUt1yX3nb3823Bean o7hB4naxmSQV5BVfUt1yX3nb3823;

        public Y1Pw8FLvkWQQLMhmPcXtQ9tvRup2Bean getY1Pw8FLvkWQQLMhmPcXtQ9tvRup2() {
            return y1Pw8FLvkWQQLMhmPcXtQ9tvRup2;
        }

        public void setY1Pw8FLvkWQQLMhmPcXtQ9tvRup2(Y1Pw8FLvkWQQLMhmPcXtQ9tvRup2Bean y1Pw8FLvkWQQLMhmPcXtQ9tvRup2) {
            this.y1Pw8FLvkWQQLMhmPcXtQ9tvRup2 = y1Pw8FLvkWQQLMhmPcXtQ9tvRup2;
        }

        public O7hB4naxmSQV5BVfUt1yX3nb3823Bean getO7hB4naxmSQV5BVfUt1yX3nb3823() {
            return o7hB4naxmSQV5BVfUt1yX3nb3823;
        }

        public void setO7hB4naxmSQV5BVfUt1yX3nb3823(O7hB4naxmSQV5BVfUt1yX3nb3823Bean o7hB4naxmSQV5BVfUt1yX3nb3823) {
            this.o7hB4naxmSQV5BVfUt1yX3nb3823 = o7hB4naxmSQV5BVfUt1yX3nb3823;
        }

        public static class Y1Pw8FLvkWQQLMhmPcXtQ9tvRup2Bean {
            /**
             * class_1 : {"class_id":"class_1","class_subject":"โครงสร้างข้อมูลและอัลกอลริทึม","class_day":"พุธ","class_start":"09.30","prof_id":"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2","worked_list":[{"worked_id":"c2w1"}]}
             * class_3 : {"class_id":"class_3","class_subject":"การคิดเชิงตัวเลข","class_day":"พุธ","class_start":"13.30","prof_id":"y1Pw8FLvkWQQLMhmPcXtQ9tvRup2"}
             */

            private Class1Bean class_1;
            private Class3Bean class_3;

            public Class1Bean getClass_1() {
                return class_1;
            }

            public void setClass_1(Class1Bean class_1) {
                this.class_1 = class_1;
            }

            public Class3Bean getClass_3() {
                return class_3;
            }

            public void setClass_3(Class3Bean class_3) {
                this.class_3 = class_3;
            }

            public static class Class1Bean {
                /**
                 * class_id : class_1
                 * class_subject : โครงสร้างข้อมูลและอัลกอลริทึม
                 * class_day : พุธ
                 * class_start : 09.30
                 * prof_id : y1Pw8FLvkWQQLMhmPcXtQ9tvRup2
                 * worked_list : [{"worked_id":"c2w1"}]
                 */

                private String class_id;
                private String class_subject;
                private String class_day;
                private String class_start;
                private String prof_id;
                private List<WorkedListBean> worked_list;

                public String getClass_id() {
                    return class_id;
                }

                public void setClass_id(String class_id) {
                    this.class_id = class_id;
                }

                public String getClass_subject() {
                    return class_subject;
                }

                public void setClass_subject(String class_subject) {
                    this.class_subject = class_subject;
                }

                public String getClass_day() {
                    return class_day;
                }

                public void setClass_day(String class_day) {
                    this.class_day = class_day;
                }

                public String getClass_start() {
                    return class_start;
                }

                public void setClass_start(String class_start) {
                    this.class_start = class_start;
                }

                public String getProf_id() {
                    return prof_id;
                }

                public void setProf_id(String prof_id) {
                    this.prof_id = prof_id;
                }

                public List<WorkedListBean> getWorked_list() {
                    return worked_list;
                }

                public void setWorked_list(List<WorkedListBean> worked_list) {
                    this.worked_list = worked_list;
                }

                public static class WorkedListBean {
                    /**
                     * worked_id : c2w1
                     */

                    private String worked_id;

                    public String getWorked_id() {
                        return worked_id;
                    }

                    public void setWorked_id(String worked_id) {
                        this.worked_id = worked_id;
                    }
                }
            }

            public static class Class3Bean {
                /**
                 * class_id : class_3
                 * class_subject : การคิดเชิงตัวเลข
                 * class_day : พุธ
                 * class_start : 13.30
                 * prof_id : y1Pw8FLvkWQQLMhmPcXtQ9tvRup2
                 */

                private String class_id;
                private String class_subject;
                private String class_day;
                private String class_start;
                private String prof_id;

                public String getClass_id() {
                    return class_id;
                }

                public void setClass_id(String class_id) {
                    this.class_id = class_id;
                }

                public String getClass_subject() {
                    return class_subject;
                }

                public void setClass_subject(String class_subject) {
                    this.class_subject = class_subject;
                }

                public String getClass_day() {
                    return class_day;
                }

                public void setClass_day(String class_day) {
                    this.class_day = class_day;
                }

                public String getClass_start() {
                    return class_start;
                }

                public void setClass_start(String class_start) {
                    this.class_start = class_start;
                }

                public String getProf_id() {
                    return prof_id;
                }

                public void setProf_id(String prof_id) {
                    this.prof_id = prof_id;
                }
            }
        }

        public static class O7hB4naxmSQV5BVfUt1yX3nb3823Bean {
            /**
             * class_2 : {"class_id":"class_2","class_subject":"อุทกศาสตร์","class_day":"อังคาร","class_start":"09.30","prof_id":"o7hB4naxmSQV5BVfUt1yX3nb3823","worked":{"worked_id":"c1w1"}}
             * class_4 : {"class_id":"class_4","class_subject":"แคลคูลัส 1 สำหรับวิศวะ","class_day":"จันทร์","class_start":"13.30","prof_id":"o7hB4naxmSQV5BVfUt1yX3nb3823"}
             */

            private Class2Bean class_2;
            private Class4Bean class_4;

            public Class2Bean getClass_2() {
                return class_2;
            }

            public void setClass_2(Class2Bean class_2) {
                this.class_2 = class_2;
            }

            public Class4Bean getClass_4() {
                return class_4;
            }

            public void setClass_4(Class4Bean class_4) {
                this.class_4 = class_4;
            }

            public static class Class2Bean {
                /**
                 * class_id : class_2
                 * class_subject : อุทกศาสตร์
                 * class_day : อังคาร
                 * class_start : 09.30
                 * prof_id : o7hB4naxmSQV5BVfUt1yX3nb3823
                 * worked : {"worked_id":"c1w1"}
                 */

                private String class_id;
                private String class_subject;
                private String class_day;
                private String class_start;
                private String prof_id;
                private WorkedBean worked;

                public String getClass_id() {
                    return class_id;
                }

                public void setClass_id(String class_id) {
                    this.class_id = class_id;
                }

                public String getClass_subject() {
                    return class_subject;
                }

                public void setClass_subject(String class_subject) {
                    this.class_subject = class_subject;
                }

                public String getClass_day() {
                    return class_day;
                }

                public void setClass_day(String class_day) {
                    this.class_day = class_day;
                }

                public String getClass_start() {
                    return class_start;
                }

                public void setClass_start(String class_start) {
                    this.class_start = class_start;
                }

                public String getProf_id() {
                    return prof_id;
                }

                public void setProf_id(String prof_id) {
                    this.prof_id = prof_id;
                }

                public WorkedBean getWorked() {
                    return worked;
                }

                public void setWorked(WorkedBean worked) {
                    this.worked = worked;
                }

                public static class WorkedBean {
                    /**
                     * worked_id : c1w1
                     */

                    private String worked_id;

                    public String getWorked_id() {
                        return worked_id;
                    }

                    public void setWorked_id(String worked_id) {
                        this.worked_id = worked_id;
                    }
                }
            }

            public static class Class4Bean {
                /**
                 * class_id : class_4
                 * class_subject : แคลคูลัส 1 สำหรับวิศวะ
                 * class_day : จันทร์
                 * class_start : 13.30
                 * prof_id : o7hB4naxmSQV5BVfUt1yX3nb3823
                 */

                private String class_id;
                private String class_subject;
                private String class_day;
                private String class_start;
                private String prof_id;

                public String getClass_id() {
                    return class_id;
                }

                public void setClass_id(String class_id) {
                    this.class_id = class_id;
                }

                public String getClass_subject() {
                    return class_subject;
                }

                public void setClass_subject(String class_subject) {
                    this.class_subject = class_subject;
                }

                public String getClass_day() {
                    return class_day;
                }

                public void setClass_day(String class_day) {
                    this.class_day = class_day;
                }

                public String getClass_start() {
                    return class_start;
                }

                public void setClass_start(String class_start) {
                    this.class_start = class_start;
                }

                public String getProf_id() {
                    return prof_id;
                }

                public void setProf_id(String prof_id) {
                    this.prof_id = prof_id;
                }
            }
        }
    }
}
