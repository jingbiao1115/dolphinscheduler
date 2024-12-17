# Dependent 节点

## 综述

Dependent 节点，就是**依赖检查节点**。比如 A 流程依赖昨天的 B 流程执行成功，依赖节点会去检查 B 流程在昨天是否有执行成功的实例。

## 创建任务

- 点击项目管理 -> 项目名称 -> 工作流定义，点击“创建工作流”按钮，进入 DAG 编辑页面；
- 拖动工具栏的<img src="../../../../img/tasks/icons/dependent.png" width="15"/> 任务节点到画板中。

## 任务参数

[//]: # (TODO: use the commented anchor below once our website template supports this syntax)
[//]: # (- 默认参数说明请参考[DolphinScheduler任务参数附录]&#40;appendix.md#默认任务参数&#41;`默认任务参数`一栏。)

- 默认参数说明请参考[DolphinScheduler任务参数附录](appendix.md)`默认任务参数`一栏。

| **任务参数** |                    **描述**                    |
|----------|----------------------------------------------|
| 添加依赖     | 配置依赖的上游任务.                                   |
| 检查间隔     | 检查依赖的上游任务状态间隔，默认10s.                         |
| 依赖失败策略   | 失败: 依赖的上游任务失败当前任务直接失败；等待: 依赖的上游任务失败当前任务继续等待； |
| 依赖失败等待时间 | 当依赖失败策略选择等待时，当前任务等待的时间.                      |

## 任务样例

Dependent 节点提供了逻辑判断功能，可以按照逻辑来检测所依赖节点的执行情况。

支持两种依赖模式，包括依赖于工作流和依赖于任务。依赖于任务的模式分依赖工作流中的所有任务和依赖单个任务两种情况。
依赖工作流的模式会检查所依赖的工作流的状态；依赖所有任务的模式会检查工作流中所有任务的状态；
依赖单个任务的模式会检查所依赖的任务的状态。

当 Dependent 节点结果为 success 且参数传递选项为 true 时，Dependent 节点会将该依赖项的输出参数输出给下游任务。当多个依赖项的参数名称相同时涉及到参数的优先级问题，详见[参数优先级](../parameter/priority.md)

例如，A 流程为周报任务，B、C 流程为天任务，A 任务需要 B、C 任务在上周执行成功，如图示：

![dependent_task01](../../../../img/tasks/demo/dependent_task01.png)

例如，A 流程为周报任务，B、C 流程为天任务，A 任务需要 B 或 C 任务在上周执行成功，如图示：

![dependent_task02](../../../../img/tasks/demo/dependent_task02.png)

假如，周报 A 同时还需要自身在上周二执行成功：

![dependent_task03](../../../../img/tasks/demo/dependent_task03.png)