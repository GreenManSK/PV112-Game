#version 330 core
#extension GL_ARB_separate_shader_objects : enable

layout(location = 0) in vec3 position;
layout(location = 1) in vec3 normal;
layout(location = 2) in vec2 texture_coordinate;

layout(location = 0) out vec3 fs_position;
layout(location = 1) out vec3 fs_normal;
layout(location = 2) out vec2 fs_texture_coordinate;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    fs_position = position;
    fs_normal = transpose(inverse(mat3(model))) * normal;
    fs_texture_coordinate = texture_coordinate;

    gl_Position = projection * view * model * vec4(position, 1.0);
}