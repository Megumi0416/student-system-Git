import { createRouter,createWebHistory } from 'vue-router'
import Error404 from '@/views/404.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'TEACHER'] },
      children: [
        { path: 'home',
          meta: { name: '系统首页' } ,
          component: () => import('@/views/manager/Home.vue')
        },
        { path: 'admin',
          meta: { name: '管理员信息' } ,
          component: () => import('@/views/manager/Admin.vue')
        },
        { path: 'notice',
          meta: { name: '系统公告' } ,
          component: () => import('@/views/manager/Notice.vue')
        },
        { path: 'message',
          meta: { name: '消息回复' } ,
          component: () => import('@/views/manager/Message.vue')
        },
        { path: 'student',
          meta: { name: '学生信息' } ,
          component: () => import('@/views/manager/Student.vue')
        },
        { path: 'teacher',
          meta: { name: '教师信息' } ,
          component: () => import('@/views/manager/Teacher.vue')
        },
        { path: 'course',
          meta: { name: '课程信息' } ,
          component: () => import('@/views/manager/Course.vue')
        },
        { path: 'testscores',
          meta: { name: '成绩信息' } ,
          component: () => import('@/views/manager/TestScores.vue')
        },
        { path: 'studentcourse',
          meta: { name: '成绩录入' } ,
          component: () => import('@/views/manager/StudentCourse.vue')
        },
        { path: 'college',
          meta: { name: '学院信息' } ,
          component: () => import('@/views/manager/College.vue')
        },
        { path: 'statistics',
          meta: { name: '成绩分析' } ,
          component: () => import('@/views/manager/Statistics.vue')
        },
        { path: 'arrangeexam',
          meta: { name: '安排考试' } ,
          component: () => import('@/views/manager/ArrangeExam.vue')
        },
        { path: 'teacherexams',
          meta: { name: '查看考试' } ,
          component: () => import('@/views/teacher/TeacherExams.vue')
        },
        { path: 'person',
          meta: { name: '个人资料' } ,
          component: () => import('@/views/manager/Person.vue')
        },
        { path: 'password',
          meta: { name: '修改密码' } ,
          component: () => import('@/views/manager/Password.vue')
        },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      meta: { requiresAuth: true, allowedRoles: ['STUDENT'] },
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue') },
        { path: 'person', component: () => import('@/views/front/Person.vue') },
        { path: 'courselist', component: () => import('@/views/front/CourseList.vue') },
        { path: 'studentcourse', component: () => import('@/views/front/StudentCourse.vue') },
        { path: 'message', component: () => import('@/views/front/Message.vue') },
        { path: 'scores', component: () => import('@/views/front/Scores.vue') }
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
    { path: '/register', component: () => import('@/views/Register.vue')},
    {
      path: '/:pathMatch(.*)*', // 使用 :pathMatch(.*)* 作为通配符
      name: 'Error404',
      component: Error404,
    }
    // { path: '/404', component: () => import('@/views/404.vue')},
    // { path: '/:pathMatch(.*)', redirect: '/404' },
]
})

router.beforeEach(async (to, from, next) => {
  const user = JSON.parse(localStorage.getItem('xm-user') || '{}');

  // 未登录处理
  if (to.meta.requiresAuth && !user.id) {
    next('/login');
    return;
  }

  // 权限校验
  if (to.meta.allowedRoles) {
    const hasPermission = to.meta.allowedRoles.includes(user.role);

    // 管理员/教师访问学生端
    if (to.path.startsWith('/front') && user.role !== 'STUDENT') {
      next('/manager/home');
    }
    // 学生访问管理端
    else if (to.path.startsWith('/manager') && user.role === 'STUDENT') {
      next('/front/home');
    }
    // 权限不足处理
    else if (!hasPermission) {
      next('/404');
    }
  }

  next();
});

export default router